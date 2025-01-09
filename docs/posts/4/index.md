---
title: "Setup Docker based HomeLab with Portainer and Nginx Proxy Manager!"
date: 2025-01-01
categories:
  - Docker
  - Nginx
  - DevOps
---

# Create bootable USB key

- Install [Rufus](https://rufus.ie/en/)
- Install [Ubuntu Server](https://ubuntu.com/download/server)
- Plug your USB key
- Open Rufus
- Device > Choose your USB key
- Boot selection > Choose the installed Ubuntu Server image
- Click on Start
- Validate pop ups and wait

# Install Ubuntu Server OS

- Plug the USB key
- Turn ON the machine and open the BIOS
- Set your the USB key as first boot priority and restart the machine
- Try or install Ubuntu Server
- Follow the instructions (choose default but some details)
  - Choose the base for the installation > Ubuntu Server
  - Network configuration to setup by cable or wifi
  - Guided storage configuration > Use an entire disk + Set up this disk as an LVM group
  - Storage configuration > ubuntu-lv > Edit > Size (max "???") > Set it to the max "???" > Save > Done
  - SSH configuration > Install OpenSSH server > Yes
  - Reboot now
  - Remove the plugged USB key and enter
- It will reboot
- Try to login if it worked, then you are ready!
- Check connection via SSH

```bash
# On the server machine
ip a
```

```bash
# On your work machine
ssh lovindata@192.168.1.X
```

- [Install Docker](https://docs.docker.com/engine/install/ubuntu/#installation-methods)

```bash
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
```

```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

```bash
sudo docker version
docker compose version
```

- [Install Portainer](https://docs.portainer.io/start/install-ce/server/docker/linux#deployment)

```bash
sudo docker volume create portainer_data
```

```bash
sudo docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:2.25.1
```

Go to https://192.168.1.X:9443

- [Install Nginx Proxy Manager](https://nginxproxymanager.com/guide/#quick-setup)

```bash
sudo docker volume create nginx_proxy_manager_data
sudo docker volume create nginx_proxy_manager_etc_letsencrypt
```

```bash
sudo docker run -d -p 80:80 -p 443:443 -p 81:81 --name nginx_proxy_manager --restart=always -v nginx_proxy_manager_data:/data -v nginx_proxy_manager_etc_letsencrypt:/etc/letsencrypt jc21/nginx-proxy-manager:2.12.2
```

Go to http://192.168.1.X:81

# Setup to open your services securely to the outside world

- Setup your ISP router to forward ports 80 and 443 to your server
- Get the CNAME or Internet IP of your router
- Get the Local network IP of your server (ip a on the server)
- Buy a domain name
- On domain provider site: Route @ -> CNAME or Internet IP
- On domain provider site: Route nginx -> CNAME or Internet IP
- On domain provider site: Route portainer -> CNAME or Internet IP
- On nginx > SSL certificates > Add SSL certificate > Domain Names = nginx.mydomain.topleveldomain > I Agree to the Let's Encrypt Terms of Service > Save
- Same as just above but for portainer
- On nginx: Route nginx.mydomain.topleveldomain -> Scheme http / Local network IP / Port 81 / Block Common Exploits
- On nginx: Route portainer.mydomain.topleveldomain -> Scheme https / Local network IP / Port 9443 / Block Common Exploits
- Go to nginx.mydomain.topleveldomain or portainer.mydomain.topleveldomain

# An example with OpenWebUI and Ollama

- From portainer: Select `local` > Go to `Stacks` > Go to `Add stack`
- For input `Name` put `llm` or an other name
- Select `Web editor` and paste the docker following docker compose file

```yml
services:
  # https://github.com/open-webui/open-webui
  openwebui:
    image: ghcr.io/open-webui/open-webui:v0.5.4
    restart: unless-stopped
    ports:
      - ${OPENWEBUI_PORT}:8080
    environment:
      OLLAMA_API_BASE_URL: http://ollama:${OLLAMA_PORT}
    volumes:
      - openwebui_app_backend_data:/app/backend/data
    healthcheck:
      test: "curl -f http://localhost:8080"
      interval: 10s
      timeout: 5s
      retries: 5
    depends_on:
      ollama:
        condition: service_healthy

  # https://hub.docker.com/r/ollama/ollama/tags
  ollama:
    image: ollama/ollama:0.5.4
    restart: unless-stopped
    ports:
      - ${OLLAMA_PORT}:11434
    volumes:
      - ollama_root_ollama:/root/.ollama
    healthcheck:
      test: "ollama --version && ollama ps || exit 1" # https://github.com/ollama/ollama/issues/1378#issuecomment-2436650823
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  openwebui_app_backend_data:
    driver: local
  ollama_root_ollama:
    driver: local
```

- In `Environment variables` section, click on `Add an environement variables` two times
- Add the two following environement variables
  - `OPENWEBUI_PORT` with the value `11435`
  - `OLLAMA_PORT` with the value `11434`
- Click on Deploy the stack
- On domain provider site: Route llm -> CNAME or Internet IP
- On nginx > SSL certificates > Add SSL certificate > Domain Names = llm.mydomain.topleveldomain > I Agree to the Let's Encrypt Terms of Service > Save
- On nginx: Route llm.mydomain.topleveldomain -> Scheme https / Local network IP / Port 11435 / Block Common Exploits / Websockets Support
- Go to llm.mydomain.topleveldomain
