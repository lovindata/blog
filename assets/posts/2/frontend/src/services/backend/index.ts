import axios from "axios";

export function useBackend() {
  const backend = axios.create({
    baseURL: import.meta.env.DEV ? "http://localhost:8080" : undefined,
  });
  return { backend };
}
