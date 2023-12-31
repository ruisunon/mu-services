
const HOST_URL: string = "http://localhost:8400";
const API_URL_CONTEXT_V1: string = "/api/v1";

export const environment = {
  staging: true,
  preprod: false,
  production: false,
  API_URL: HOST_URL + API_URL_CONTEXT_V1,
};
