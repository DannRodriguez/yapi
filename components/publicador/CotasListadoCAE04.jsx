import { Col, Row } from "antd";
import React, { useEffect, useState } from "react";
import "../../css/publicador.scss";
import { obtenerListaCausasVacante } from "../../utils/causaSustitucion/funciones.js";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { CODE_SUCCESS } from "../../utils/constantes.js";
import { Loader } from "../interfaz/Loader.jsx";

export function CotasListadoCAE04() {
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
                        <span className="span_cotas_tipos">Terminación anticipada: </span>
                    </div>
                </Col>
                <br />
                <br />
                <Col xs={15} md={19} xl={15}>
                    <div className="div_cotas_tipos-container">
                        <span className="span_cotas_tipos"><b>Otras causas: </b> <br />
                            <b>1.-</b> Fallecimiento <br />
                            <b>2.-</b> Promoción <br />
                            <b>3.-</b> Declinación al contrato <br />
                            <b>4.-</b> Incapacidad <br />
                        </span>
                    </div>
                </Col>

            </Row>
        </>
    );
}