import { Col, Row } from "antd";
import React, { useEffect, useState } from "react";
import "../../css/publicador.scss";
import { obtenerListaCausasVacante } from "../../utils/causaSustitucion/funciones.js";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { CODE_SUCCESS } from "../../utils/constantes.js";
import { Loader } from "../interfaz/Loader.jsx";

export function CotasListadoCAE01() {
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
                    <div className="div_cotas_tipos-container">
                        <span className="span_cotas_tipos">Sexo: <b>H: </b>Hombre  <b>M: </b>Mujer <b>NB: </b> No Binario <b>O: </b> Otro <b>NE: </b> No especificó </span>
                    </div>
                </Col>
                <br />
                <br />
                <Col xs={15} md={19} xl={15}>
                    <div className="div_cotas_tipos-container">
                        <span className="span_cotas_tipos"><b>ZORE.-</b> Zona de Responsabilidad Electoral <br />
                            <b>ARE.-</b> Areá de Responsabilidad Electoral <br />
                            <b>1.-</b> ZORE o ARE que tiene asignada durante la 1a etapa <br />
                            <b>2.-</b> ZORE o ARE que tiene asignada durante la 2da etapa
                        </span>
                    </div>
                </Col>

            </Row>
        </>
    );
}