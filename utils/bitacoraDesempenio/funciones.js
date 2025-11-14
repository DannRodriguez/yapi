import apiClient from "../apiClient";

export function obtenerAspiranteBitacora(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("obtenerAspiranteBitacora", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -obtenerAspiranteBitacora: ", error);
        reject(new Error(error));
      });
  });
}

export function obtenerExpedienteDesempenio(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("obtenerExpedienteDesemp", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -obtenerExpedienteDesempenio: ", error);
        reject(new Error(error));
      });
  });
}

export function obtenerDocumentoExpediente(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("obtenerExpedienteB64", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -obtenerDocumentoExpediente: ", error);
        reject(new Error(error));
      });
  });
}

export function obtenerEvaluacionDesempenio(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("obtenerEvaluacionDesemp", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -obtenerEvaluacionDesempenio: ", error);
        reject(new Error(error));
      });
  });
}

export function guardarBitacoraDesemp(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("guardarBitacoraDesemp", request, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -guardarBitacoraDesemp: ", error);
        reject(new Error(error));
      });
  });
}

export function eliminarBitacoraDesemp(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("eliminarBitacoraDesemp", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR -eliminarBitacoraDesemp: ", error);
        reject(new Error(error));
      });
  });
}

export function validarFormatoExpediente(tipoFormato, nombreArchivo) {
  return (
    tipoFormato == "image/gif" ||
    tipoFormato == "image/png" ||
    tipoFormato == "image/jpeg" ||
    tipoFormato == "application/zip" ||
    tipoFormato == "application/pdf" ||
    tipoFormato == "application/msword" ||
    tipoFormato ==
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ||
    tipoFormato == "application/vnd.ms-excel" ||
    tipoFormato ==
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
    tipoFormato == "application/vnd.ms-powerpoint" ||
    tipoFormato ==
    "application/vnd.openxmlformats-officedocument.presentationml.presentation" ||
    nombreArchivo.endsWith(".msg") ||
    nombreArchivo.endsWith(".rar")
  );
}

export function obtenerIdAspirantePorFolio(request) {
  return new Promise((resolve, reject) => {
    apiClient
      .post("obtenerIdAspirantePorFolio", request)
      .then((response) => {
        resolve(response.data);
      })
      .catch((error) => {
        console.error("ERROR - obtenerIdAspirantePorFolio: ", error);
        reject(new Error(error));
      });
  });
}

export function getDimensionArchivo(fileSize) {
  let dimensionArchivo = "";

  if (fileSize < 1048576) {
    //menor a 1 MB
    let size = fileSize / 1024;
    size = Number.parseFloat(size).toFixed(2);
    dimensionArchivo = " - " + size + " KB";
  } else {
    //más de 1 MB
    let size = fileSize / 1024 / 1024;
    size = Number.parseFloat(size).toFixed(2);
    dimensionArchivo = " - " + size + " MB";
  }

  return dimensionArchivo;
}