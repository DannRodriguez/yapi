import apiClient from "../apiClient";

export function consultaDeshacerSustitucion(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("consultaDeshacerSustitucion", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -consultaDeshacerSustitucion: ", error);
        reject(new Error(error));
      });
  });
}

export function consultaSustitucionesDeshechas(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("consultaSustitucionesDeshechas", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -consultaSustitucionesDeshechas: ", error);
        reject(new Error(error));
      });
  });
}

export function deshacerSustitucion(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("deshacerSustitucion", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -deshacerSustitucion: ", error);
        reject(new Error(error));
      });
  });
}
