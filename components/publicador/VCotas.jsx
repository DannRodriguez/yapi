import React from 'react';
import { InfoCircleOutlined, RightOutlined } from '@ant-design/icons'
import *  as etiquetas from '../../utils/publicador/etiquetas';

const VCotas = ({ cotas, isOpenCotas, handleChange }) => {
    return(
            <div className='publicador-cotas-container'>
                <div className='pcc-icon'
                    style={{display:`${isOpenCotas ? 'none': 'flex'}`}}
                    onClick={handleChange} >
                    <InfoCircleOutlined />
                </div>
                <div className='pcc-icon'
                    style={{display:`${isOpenCotas ? 'flex': 'none'}`}}
                    onClick={handleChange}>
                    <RightOutlined />
                </div>
                <div className='pcc-data'
                    style={{display:`${isOpenCotas ? 'flex': 'none'}`}}>
                    <div className='pcc-data-title'>
                        <InfoCircleOutlined />
                        <span className='pcc-data-title-text'>{etiquetas.COTAS_TITLE}</span>
                    </div>
                    {
                        cotas.map((cotaGroup, index) => {
                            return(
                                <div className='pcc-data-group' key={index}>
                                    <span className='pcc-data-group-title' >{cotaGroup.title}</span>
                                    {
                                    cotaGroup.content.map((cota, index) => {
                                        return(
                                            <div className='pcc-data-group-content' key={index}>
                                                <span>{cota.label}</span>
                                                <span>{cota.value}</span>
                                            </div>
                                        );
                                    })
                                    }
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        );
}

export default VCotas;