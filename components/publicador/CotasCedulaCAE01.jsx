import { Col, Row } from "antd";
import React, { useEffect, useState } from "react";
import "../../css/publicador.scss";
import { obtenerListaCausasVacante } from "../../utils/causaSustitucion/funciones.js";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { CODE_SUCCESS } from "../../utils/constantes.js";
import { Loader } from "../interfaz/Loader.jsx";

export function CotasCedulaCAE01() {
  const user = useSelector((store) => store.loginUser.currentUser);

  const [obtenerCausas, setObtenerCausas] = useState(1);
  const [listCausas, setListCausas] = useState([]);
  const [block, setBlock] = useState(false);

  useEffect(() => {
    if (obtenerCausas) {
      setObtenerCausas(0);
      obtenerListaCausasVacante({})
        .then((data) => {
          let code = data.code;
          if (code == CODE_SUCCESS) {
            setListCausas(data.data);
            setBlock(false);
          } else {
            setBlock(false);
          }
        })
        .catch((error) => {
          console.log("Ocurrió un error al obtener las causas, " + error);
          setBlock(false);
        });
    }
  }, []);

  return (
    <>
      <Loader blocking={block} />
      <Row style={{ marginLeft: "30px" }}>
        <Col xs={15} md={19} xl={15}>
          <br />
          <div className="div_cotas_tipos-container">
            <span className="span_cotas_tipos">Personal contratado: </span>
            <span
              className="span_cuadro1"
              style={{ background: "#FFFFFF" }}
            ></span>
          </div>
          <div style={{ marginTop: "10px" }}>
            <span className="span_cotas_motivo">
              1. Total del personal contratado de SE y CAE.
            </span>
          </div>
        </Col>
        <Col xs={15} md={13} xl={8} style={{ marginLeft: "20px" }}>
          <div className="div_cotas_tipos-container">
            <span className="span_cotas_tipos">Terminación Anticipada:</span>
            <span
              className="span_cuadro1"
              style={{ background: "#ED92C8" }}
            ></span>
          </div>

          {listCausas
            .filter((opcion) => opcion.id.tipoCausaVacante === 2)
            .map((opcion) => (
              <span
                className="span_cotas_motivo"
                key={opcion.id.idCausaVacante}
              >
                {opcion.id.idCausaVacante + ".- " + opcion.descripcion + "."}
              </span>
            ))}
          {/* <div style={{marginTop:"10px"}}>
                        <span className="span_cotas_motivo">1. Cambio de domicilio 'CVT2_1'.</span>
                        <span className="span_cotas_motivo">2. Oferta laboral 'CVT2_2'.</span>
                        <span className="span_cotas_motivo">3. Enfermedad 'CVT2_3'.</span>
                        <span className="span_cotas_motivo">4. Incompatibilidad con otras actividades 'CVT2_4'.</span>
                        <span className="span_cotas_motivo">5. Por motivos personales 'CVT2_5'.</span>
                    </div> */}
        </Col>
      </Row>
      <Row style={{ marginLeft: "30px", marginTop: "20px" }}>
        <Col xs={15} md={19} xl={15}>
          <div className="div_cotas_tipos-container">
            <span className="span_cotas_tipos">Rescisión de contrato: </span>
            <span
              className="span_cuadro1"
              style={{ background: "#FBEDF6" }}
            ></span>
          </div>

          <div style={{ marginTop: "10px" }}>
            {listCausas
              .filter((opcion) => opcion.id.tipoCausaVacante === 1)
              .map((opcion) => (
                <span
                  className="span_cotas_motivo"
                  key={opcion.id.idCausaVacante}
                >
                  {opcion.id.idCausaVacante + ".- " + opcion.descripcion + "."}
                </span>
              ))}
          </div>

          {/* <div style={{marginTop:"10px"}}>
                        <span className="span_cotas_motivo">1. Incurrir en falsedad 'CV1'.</span>
                        <span className="span_cotas_motivo">2. Inadecuada atención a ciudadanos y/o compañeros de trabajo 'CV2'.</span>
                        <span className="span_cotas_motivo">3. Dañar y poner en peligro los bienes del Instituto Nacional Electoralc 'CV3'.</span>
                        <span className="span_cotas_motivo">4. Violar la disciplina institucional 'CV4'.</span>
                        <span className="span_cotas_motivo">5. Dejar de cumplir con los requisitos señalados en la Convocatoria 'CV5'.</span>
                        <span className="span_cotas_motivo">6. Asistir a prestar sus servicios en estado de ebriedad o bajo los influjos de drogas o estupefacientes, sin prescripción médica 'CV6'.</span>
                        <span className="span_cotas_motivo">7. Difundir información confidencial 'CV7'. </span>
                        <span className="span_cotas_motivo">8. Mantener contacto con partidos, candidatos u organizaciones políticas, en contravención de las obligaciones propias de cada figura 'CV8'.</span>
                        <span className="span_cotas_motivo">9. Entregar documentación falsa o alterada al INE 'CV9'. </span>
                        <span className="span_cotas_motivo">10. Dejar de prestar el servicio para el que fueron contratados 'CV10'.</span>
                        <span className="span_cotas_motivo">11. Incumplimiento de las actividades para las que fueron contratados 'CV11'. </span>
                        <span className="span_cotas_motivo">12. Cualquier otra causa de gravedad 'CV12'.</span>
                        <span className="span_cotas_motivo">13. Ser afiliado o militante de partído político 'CV13'.</span>
                        <span className="span_cotas_motivo">13. Ser afiliado o militante de partído político 'CV13'.</span>

                    </div> */}
        </Col>
        <Col xs={15} md={13} xl={8} style={{ marginLeft: "20px" }}>
          <div className="div_cotas_tipos-container">
            <span className="span_cotas_tipos">Otras causas:</span>
            <span
              className="span_cuadro1"
              style={{ background: "#F9DBED" }}
            ></span>
          </div>
          <div style={{ marginTop: "10px" }}>
            <span className="span_cotas_motivo">1.- Fallecimiento.</span>
            <span className="span_cotas_motivo">2.- Promoción.</span>
            <span className="span_cotas_motivo">
              3.- Declinación al contrato.
            </span>
            <span className="span_cotas_motivo">4.- Incapacidad.</span>
          </div>

          <div
            className="div_cotas_tipos-container"
            style={{ marginTop: "20px" }}
          >
            <span className="span_cotas_tipos">Fórmulas:</span>
          </div>
          <div style={{ marginTop: "10px" }}>
            <span className="span_cotas_motivo">
              % de rescisiones = (Total rescisiones * 100)/(Total de
              Sustituciones de SE y CAE){" "}
            </span>
            <span className="span_cotas_motivo">
              % de renuncias = (Total renuncias * 100)/(Total de Sustituciones
              de SE y CAE)
            </span>
            <span className="span_cotas_motivo">
              % de otras causas = (Total otras * 100)/(Total de Sustituciones de
              SE y CAE)
            </span>
            <span className="span_cotas_motivo">
              CAE.- Cacapacitador Asistente Electoral promocionado a SE
            </span>
          </div>
        </Col>
      </Row>
    </>
  );
}
