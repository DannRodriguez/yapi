import React from 'react';
import { ArrowLeftOutlined } from '@ant-design/icons';
import VCotas from './VCotas';
import VExportar from './VExportar';
import VTabla from './VTabla';
import VCustomModal from '../commonComponents/VCustomModal';
import * as etiquetas from '../../utils/publicador/etiquetas';
import { obtieneEtiquetaNivel } from '../../utils/publicador/funciones';
import { obtenerDocumentoExpediente, obtenerIdAspirantePorFolio } from '../../utils/bitacoraDesempenio/funciones';
import { CODE_SUCCESS } from '../../utils/constantes';
import { log } from 'exceljs/dist/exceljs';

const handleLinkToNivelAddProps = (proceso, estado, distrito, municipio, handleLinkToNivel, record, nivel) => {
    handleLinkToNivel(nivel,
        record.idDetalleProceso ? {
            descripcionDetalle: record.descripcionDetalle,
            idDetalleProceso: record.idDetalleProceso
        } : proceso,
        record.idEstado ? {
            nombreEstado: record.nombreEstado,
            idEstado: record.idEstado
        } : estado,
        record.idDistrito ? {
            nombreDistrito: record.nombreDistrito !== undefined ? record.nombreDistrito : (record.cabeceraDistritalLocal != undefined ? record.cabeceraDistritalLocal : record.cabeceraDistritalFederal),
            idDistrito: record.idDistrito
        } : distrito,
        record.idMunicipio ? {
            nombreMunicipio: record.nombreMunicipio,
            idMunicipio: record.idMunicipio
        } : municipio);
}

const VReporte = ({ tipoReporte, reporte, nivel,
    proceso, estado, distrito, municipio,
    error, datosTabla, datosFiltrados, isVistaTemporal,
    handleChangeTabla, handleLinkToNivel, handleReturn,
    isOpenCotas, handleChangeCotas }) => {

    const [expedientesDisponibles, setExpedientesDisponibles] = React.useState({});
    const [openModal, setOpenModal] = React.useState(false);
    const [msjModal, setMsjModal] = React.useState("");
    const [tipoModal, setTipoModal] = React.useState(2);

    const columnasWeb = datosTabla?.header?.web ? [...datosTabla.header.web] : [];

    const cerrarModal = () => {
        setOpenModal(false);
        setMsjModal("");
    };

    if (tipoReporte === "Listados" && reporte?.label?.startsWith("SustSEyCAE-4")) {
        columnasWeb.push({
            title: "Expediente",
            key: "expediente",
            dataIndex: "expediente",
            width: 100,
            align: "center",
            render: (_, record) => {

                const recordKey = `${record.folio}-${record.idEstado}-${record.idDistrito}`;
                return (
                    <a
                        href="#"
                        onClick={async (e) => {
                            e.preventDefault();

                            try {
                                const requestFolio = {
                                    folio: parseInt(record.folio) || Number(record.folio),
                                    idEstado: record.idEstado || estado?.idEstado,
                                    idDistrito: record.idDistrito || distrito?.idDistrito
                                };

                                const responseIdAspirante = await obtenerIdAspirantePorFolio(requestFolio);

                                if (responseIdAspirante.code === CODE_SUCCESS) {

                                    const { idParticipacion, idAspirante } = responseIdAspirante.data;


                                    const requestExpediente = {
                                        idDetalleProceso: record.idDetalleProceso || proceso?.idDetalleProceso,
                                        idParticipacion: idParticipacion,
                                        idAspirante: idAspirante,
                                    };

                                    const data = await obtenerDocumentoExpediente(requestExpediente);

                                    if (data.code === CODE_SUCCESS) {
                                        let expedienteB64 = data.data.body || data.data;

                                        if (expedienteB64) {
                                            const extension = record.nombreDocumento
                                                ? record.nombreDocumento.substring(record.nombreDocumento.lastIndexOf('.'))
                                                : '.pdf';
                                            const nombreArchivo = `Expediente${extension}`;

                                            const linkSource = expedienteB64;
                                            const downloadLink = document.createElement("a");
                                            const fileName = nombreArchivo;
                                            downloadLink.href = linkSource;
                                            downloadLink.download = fileName;
                                            downloadLink.click();
                                        } else {
                                            setMsjModal("No hay expediente disponible.");
                                            setTipoModal(2);
                                            setOpenModal(true);
                                        }
                                    } else {
                                        setMsjModal("No hay expediente disponible.");
                                        setTipoModal(2);
                                        setOpenModal(true);
                                    }
                                } else {
                                    setMsjModal("No hay expediente disponible.");
                                    setTipoModal(2);
                                    setOpenModal(true);
                                }
                            } catch (error) {
                                console.error("Error:", error);
                                setMsjModal("No hay expediente disponible.");
                                setTipoModal(2);
                                setOpenModal(true);
                            }
                        }}
                    >
                        Descargar
                    </a>
                );
            }
        });
    }

    return (
        <div className='publicador-reporte-container'>
            {
                datosTabla ?
                    <>
                        {datosTabla.cotas
                            && datosTabla.cotas.html
                            && datosTabla.cotas.html.length > 0 ?
                            <VCotas cotas={datosTabla.cotas.html}
                                isOpenCotas={isOpenCotas}
                                handleChange={handleChangeCotas} />
                            : ''
                        }
                        {isVistaTemporal ?
                            <div className='rc-regresar'
                                onClick={handleReturn}>
                                <ArrowLeftOutlined />{etiquetas.ACCION_REGRESAR}
                            </div>
                            : ''
                        }
                        <div className='rc-reporte'>
                            <span className='rc-reporte-title'>{reporte?.label}</span>
                            <span className='rc-reporte-nivel'>{obtieneEtiquetaNivel(estado, distrito, municipio, nivel)}</span>
                        </div>
                        <VExportar proceso={proceso}
                            estado={estado}
                            distrito={distrito}
                            municipio={municipio}
                            reporte={reporte}
                            nivel={nivel}
                            cotas={datosTabla.cotas && datosTabla.cotas.pdf ?
                                datosTabla.cotas.pdf
                                : []}
                            headerCSV={datosTabla.header.csv}
                            headerPDF={datosTabla.header.pdf}
                            datos={datosFiltrados && datosFiltrados.length > 0 ?
                                datosFiltrados
                                : datosTabla.datos} />
                        <VTabla tipoReporte={tipoReporte}
                            header={columnasWeb}
                            datos={datosTabla.datos}
                            handleChange={handleChangeTabla}
                            handleLinkToNivelAddProps={handleLinkToNivelAddProps.bind(null,
                                proceso,
                                estado,
                                distrito,
                                municipio,
                                handleLinkToNivel)} />
                    </>
                    : <div className='rc-error'>
                        <span className='rc-error-msg'>
                            {error ? error : etiquetas.NO_HAY_DATOS}
                        </span>
                    </div>
            }
            <VCustomModal
                title={null}
                mensaje={msjModal}
                footer={null}
                open={openModal}
                tipoModal={tipoModal}
                cerrarModal={cerrarModal}
            />
        </div>
    );
}

export default VReporte;