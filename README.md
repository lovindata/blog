<p align="center">
  <a href="https://lovindata.github.io/blog/" target="_blank">
      <img alt="LovinData" src="docs/assets/logo.png" width="150" style="max-width: 100%;">
  </a>
</p>

<p align="center">
  LovinData - Simplified Full Stack Data Engineering
</p>

<p align="center">
    <a href="https://github.com/lovindata/blog/actions"><img src="https://img.shields.io/github/actions/workflow/status/lovindata/blog/ci.yml?branch=main" alt="Build Status"></a>
    <a href="https://github.com/lovindata/blog/blob/main/LICENSE"><img src="https://img.shields.io/github/license/lovindata/blog" alt="License"></a>
</p>

---

## Contribution

Please install [VSCode](https://code.visualstudio.com/) and its extensions:

- Even Better TOML
- Prettier

Please install [Python](https://www.python.org/downloads/), [pip](https://pip.pypa.io/en/stable/installation/) and [venv](https://docs.python.org/3/library/venv.html).

```bash
sudo apt install python3 python3-pip python3-venv
```

Please install [Poetry](https://python-poetry.org/docs/#installing-with-the-official-installer) with the official installer.

```bash
curl -sSL https://install.python-poetry.org | python3 -
echo -e '\nexport PATH="/home/lovindata/.local/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
poetry config virtualenvs.in-project true
```

To create your Python environment and install dependencies:

```bash
poetry install
```

To update dependencies:

```bash
poetry update
```

To clear poetry cache:

```bash
poetry cache clear --all --quiet .
```

To serve the blog, run the command:

```bash
poetry run mkdocs serve
```
