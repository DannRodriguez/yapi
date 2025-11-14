import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";

import AuthenticatedComponent from "../AuthenticatedComponent";
import Template from "../interfaz/Template";
import * as Constantes from "../../utils/constantes";
import { Layout, Table, Button, Modal } from "antd";
import Miga from "../commonComponents/Miga";
import HeaderModulo from "../commonComponents/HeaderModulo";
import { Loader } from "../interfaz/Loader";
import {
  consultaDeshacerSustitucion,
  deshacerSustitucion,
  consultaSustitucionesDeshechas,
} from "../../utils/deshacerSustituciones/funciones";
import {
  ExclamationCircleFilled,
  CheckCircleOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { CMenuAcciones } from "../interfaz/navbar/CMenuAcciones.jsx";

export default function DeshacerSustituciones() {
  const [vistaActual, setVistaActual] = useState(Constantes.FLUJO_CAPTURA);
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);
  const [dataConsulta, setDataConsulta] = useState([]);

  const [openModal, setOpenModal] = useState(false);
  const [tipoModal, setTipoModal] = useState(1); //1 modal ok; 2 modal error
  const [msjModal, setMsjModal] = useState("");
  const navigate = useNavigate();
  const menuAcciones = useSelector((store) => store.menu.menuAcciones);

  let noFila = 1;

  let noFilaConsulta = 1;

  const menu = useSelector((store) => store.menu);

  const user = useSelector((store) => store.loginUser.currentUser);

  useEffect(() => {
    let data = {
      idProcesoElectoral: menu.proceso.idProcesoElectoral,
      idDetalleProceso: menu.proceso.idDetalleProceso,
      idParticipacion: menu.idParticipacion,
    };
    setLoading(true);
    consultaDeshacerSustitucion(data)
      .then((data) => {
        if (data.status == 200) {
          console.log("Respuesta info: ", data.entity.data);
          setData(data.entity.data);
        } else {
          mostrarModal(data.entity.message, 2);
        }
        setLoading(false);
      })
      .catch((error) => {
        console.log("Error consultar tabla: ", error);
        mostrarModal(error, 2);
        setLoading(false);
      });
  }, []);

  useEffect(() => {
    noFilaConsulta = 1;
    if (vistaActual == Constantes.FLUJO_CONSULTA) {
      let data = {
        idProcesoElectoral: menu.proceso.idProcesoElectoral,
        idDetalleProceso: menu.proceso.idDetalleProceso,
        idParticipacion: menu.idParticipacion,
      };
      setLoading(true);
      consultaSustitucionesDeshechas(data)
        .then((data) => {
          if (data.status == 200) {
            console.log("Respuesta info: ", data.entity.data);
            setDataConsulta(data.entity.data);
          } else {
            mostrarModal(data.entity.message, 2);
          }
          setLoading(false);
        })
        .catch((error) => {
          console.log("Error consultar tabla: ", error);
          mostrarModal(error, 2);
          setLoading(false);
        });
    }
  }, [vistaActual]);

  const mostrarModal = (mssg, tipoVentanaModal) => {
    setMsjModal(mssg);
    setOpenModal(true);
    setTipoModal(tipoVentanaModal);
  };

  const cerrarModal = () => {
    setMsjModal("");
    setOpenModal(false);
    if (tipoModal == 1) navigate(0);
  };

  function mandaDeshacerSustitucion(sustitucionADeshacer) {
    let data = {
      idProcesoElectoral: menu.proceso.idProcesoElectoral,
      idDetalleProceso: menu.proceso.idDetalleProceso,
      idParticipacion: menu.idParticipacion,
      user: user.username,
      sustitucionADeshacer,
    };
    setLoading(true);
    deshacerSustitucion(data)
      .then((data) => {
        if (data.status == 200) {
          console.log("Respuesta info: ", data.entity.data);
          mostrarModal("¡El registro se deshizo con éxito!", 1);
        } else {
          mostrarModal(data.entity.message, 2);
        }
        setLoading(false);
      })
      .catch((error) => {
        console.log("Error consultar tabla: ", error);
        mostrarModal(error, 2);
        setLoading(false);
      });
  }

  const sharedOnCell = (record, index) => {
    let valor = index;
    //console.log("valor ", valor)
    //console.log("record: ",record)
    if (vistaActual == Constantes.FLUJO_CONSULTA) {
      if (dataConsulta[valor + 1]) {
        //console.log("existe")
        if (
          dataConsulta[valor]?.id_relacion_sustituciones ==
          dataConsulta[valor + 1]?.id_relacion_sustituciones
        ) {
          return {
            rowSpan: 2,
          };
        }
      }
      if (dataConsulta[valor - 1]) {
        //console.log("existe")
        if (
          dataConsulta[valor]?.id_relacion_sustituciones ==
          dataConsulta[valor - 1]?.id_relacion_sustituciones
        ) {
          return {
            rowSpan: 0,
          };
        }
      }
      return {};
    } else {
      if (data[valor + 1]) {
        //console.log("existe")
        if (
          data[valor]?.id_relacion_sustituciones ==
          data[valor + 1]?.id_relacion_sustituciones
        ) {
          return {
            rowSpan: 2,
          };
        }
      }
      if (data[valor - 1]) {
        //console.log("existe")
        if (
          data[valor]?.id_relacion_sustituciones ==
          data[valor - 1]?.id_relacion_sustituciones
        ) {
          return {
            rowSpan: 0,
          };
        }
      }
      return {};
    }
  };

  const devuelveNumero = (index) => {
    let valor = index;
    if (vistaActual == Constantes.FLUJO_CONSULTA) {
      if (dataConsulta[valor + 1]) {
        //console.log("existe")
        if (
          dataConsulta[valor]?.id_relacion_sustituciones ==
          dataConsulta[valor + 1]?.id_relacion_sustituciones
        ) {
          let valorDevuelve = noFilaConsulta;
          noFilaConsulta = noFilaConsulta + 1;
          return valorDevuelve;
        }
      }
      if (dataConsulta[valor - 1]) {
        //console.log("existe")
        if (
          dataConsulta[valor]?.id_relacion_sustituciones ==
          dataConsulta[valor - 1]?.id_relacion_sustituciones
        ) {
          return noFilaConsulta;
        }
      }
      let valorDevuelve = noFilaConsulta;
      noFilaConsulta = noFilaConsulta + 1;
      return valorDevuelve;
    } else {
      if (data[valor + 1]) {
        //console.log("existe")
        if (
          data[valor]?.id_relacion_sustituciones ==
          data[valor + 1]?.id_relacion_sustituciones
        ) {
          let valorDevuelve = noFila;
          noFila = noFila + 1;
          return valorDevuelve;
        }
      }
      if (data[valor - 1]) {
        //console.log("existe")
        if (
          data[valor]?.id_relacion_sustituciones ==
          data[valor - 1]?.id_relacion_sustituciones
        ) {
          return noFila;
        }
      }
      let valorDevuelve = noFila;
      noFila = noFila + 1;
      return valorDevuelve;
    }
  };

  const validaUnion = (index) => {
    let valor = index;
    if (data[valor + 1]) {
      console.log("existe");
      if (
        data[valor]?.id_relacion_sustituciones ==
        data[valor + 1]?.id_relacion_sustituciones
      ) {
        console.log("devuele true");
        return true;
      }
    }
    if (data[valor - 1]) {
      console.log("existe");
      if (
        data[valor]?.id_relacion_sustituciones ==
        data[valor - 1]?.id_relacion_sustituciones
      ) {
        console.log("devuele true");
        return true;
      }
    }
    console.log("devuele false");
    return false;
  };

  const columns = [
    {
      title: "No.",
      dataIndex: "id_sustitucion",
      key: "id_sustitucion",
      onCell: sharedOnCell,
      render: (text, record, index) => <>{devuelveNumero(index)}</>,
    },
    {
      title: "Cargo de la persona sustituida",
      dataIndex: "puesto_sustituido",
      key: "nombre_sustituido",
    },
    {
      title: "Nombre de la persona sustituida",
      dataIndex: "nombre_sustituido",
      key: "nombre_sustituido",
    },
    {
      title: "Causa de la sustitución",
      dataIndex: "causa",
      key: "causa",
    },
    {
      title: "Fecha de baja",
      dataIndex: "fecha_baja",
      key: "fecha_baja",
    },
    {
      title: "Nombre de la persona sustituta",
      dataIndex: "nombre_sustituto",
      key: "nombre_sustituto",
      render: (text, record, index) => (
        <>
          {record.id_aspirante_sustituto
            ? record.nombre_sustituto
            : "Sin asignar"}
        </>
      ),
    },
    {
      title: "Fecha de alta",
      dataIndex: "fecha_alta",
      key: "fecha_alta",
      render: (text, record, index) => (
        <>{record.id_aspirante_sustituto ? record.fecha_alta : "N/A"}</>
      ),
    },
    {
      title: "Procedencia",
      dataIndex: "puesto_sustituto",
      key: "puesto_sustituto",
      render: (text, record, index) => (
        <>{record.id_aspirante_sustituto ? record.puesto_sustituto : "N/A"}</>
      ),
    },
    {
      title: "Acciones",
      dataIndex: "id_relacion_sustituciones",
      key: "id_relacion_sustituciones",
      render: (text, record, index) => (
        <Button
          hidden={vistaActual == Constantes.FLUJO_CONSULTA}
          disabled={record?.existEnSustPosteriores}
          className={
            record?.existEnSustPosteriores ? "btnDeshDisabled" : "btnDeshacer"
          }
          onClick={() => mandaDeshacerSustitucion(record)}
        >
          Deshacer
        </Button>
      ),
      onCell: sharedOnCell,
    },
  ];

  function actualizaDatos(pagination) {
    if (pagination?.current == 1) {
      noFila = 1;
      noFilaConsulta = 1;
    }
  }

  function vistaRenderizada() {
    switch (vistaActual) {
      case Constantes.FLUJO_CAPTURA:
        return (
          <>
            <Layout id="content-bitacora">
              <Miga />
              <br />
              <HeaderModulo />
              <br />
              <Table
                key={"captura"}
                className="tableDesSust"
                columns={columns}
                pagination={false}
                dataSource={data}
                bordered
                onChange={(pagination) => actualizaDatos(pagination)}
                /* rowClassName={(record, index) => validaUnion(index) == true ? 'row-gray' : 'row-white'} */
              />
            </Layout>
          </>
        );
      default:
        return (
          <>
            <Layout id="content-bitacora">
              <Miga />
              <br />
              <HeaderModulo />
              <br />
              <Table
                key={"consulta"}
                className="tableDesSust"
                columns={columns}
                pagination={false}
                dataSource={dataConsulta}
                bordered
                onChange={(pagination) => actualizaDatos(pagination)}
                /* rowClassName={(record, index) => validaUnion(index) == true ? 'row-gray' : 'row-white'} */
              />
            </Layout>
          </>
        );
    }
  }

  return (
    <AuthenticatedComponent>
      <Loader blocking={loading} />
      <Template>
        <CMenuAcciones
          state={vistaActual}
          setState={setVistaActual}
          menuAcciones={menuAcciones}
        />
        {vistaRenderizada()}

        <Modal
          title={null}
          className="modal-util-opc"
          open={openModal}
          centered
          footer={null}
          onCancel={() => cerrarModal()}
          maskClosable={false}
        >
          <div className="divCenterUtil">
            {tipoModal == 1 ? (
              <CheckCircleOutlined className="icono_antd_ok" id="ok" />
            ) : (
              <ExclamationCircleFilled
                className="icono_antd_warn"
                id="warning"
              />
            )}
          </div>
          <br />
          <div className="divCenterUtil">
            <p className="msj-modal-gen">{msjModal}</p>
          </div>
        </Modal>
      </Template>
    </AuthenticatedComponent>
  );
}
