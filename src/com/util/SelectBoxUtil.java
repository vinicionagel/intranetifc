/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import com.dto.CampusDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.SetorDTO;
import com.dto.TipoServicoDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;

/**
 *
 * @author vinicio
 */
public class SelectBoxUtil {

    /**
     * Retorna lista de patrimonio ordenado por campus e localizacao;
     *
     * @param patrimonios odenado por campus e localizacao;
     * @return Patrimonios(SelectItem) da seguinte forma .. Campus Rio do Sul<br>
     * .... Laboratorio 101 <br> ...... Micro10
     */
    public List<SelectItem> retornaListaPatrimonioSelectItem(List<PatrimonioDTO> patrimonios) {
        List<SelectItem> items = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        LocalizacaoDTO localizacao = null;
        Map<Long, CampusDTO> checkCampus = new HashMap<>();
        for (int i = 0; i < patrimonios.size(); i++) {
            if (localizacao == null) {
                localizacao = patrimonios.get(i).getLocalizacaoDTO();
                if (!checkCampus.containsKey(patrimonios.get(i).getLocalizacaoDTO().getCampusDTO().getCodigo())) {
                    builder.append("... ");
                    builder.append(patrimonios.get(i).getLocalizacaoDTO().getCampusDTO().getDescricao());
                    items.add(new SelectItem(null, builder.toString(), null, true, true, true));
                    builder.delete(0, builder.length());
                    checkCampus.put(patrimonios.get(i).getLocalizacaoDTO().getCampusDTO().getCodigo(),
                            patrimonios.get(i).getLocalizacaoDTO().getCampusDTO());
                    items.add(new SelectItem(null, construtorLocalizacao(localizacao, builder), null, true, true, true));
                    builder.delete(0, builder.length());
                    items.add(new SelectItem(patrimonios.get(i).getCodigo(),
                            construtorPatrimonio(patrimonios.get(i), builder)));
                    builder.delete(0, builder.length());
                } else {
                    items.add(new SelectItem(null, construtorLocalizacao(localizacao, builder), null, true, true, true));
                    builder.delete(0, builder.length());
                    items.add(new SelectItem(patrimonios.get(i).getCodigo(),
                        construtorPatrimonio(patrimonios.get(i), builder)));
                    builder.delete(0, builder.length());
                }
            } else if (localizacao != patrimonios.get(i).getLocalizacaoDTO()) {
                localizacao = null;
                i--;
            } else {
                items.add(new SelectItem(patrimonios.get(i).getCodigo(),
                        construtorPatrimonio(patrimonios.get(i), builder)));
                builder.delete(0, builder.length());
            }
        }
        return items;
    }

    /**
     * Método para retornar uma lista de setores do usuario da seguinte forma
     * <b><br>... Campus Rio do Sul <br> ...... Informática</b> assim por
     * diante;<br> <b>o parametro deve ser o usuario logado e uma lista de
     * Setores</b>
     *
     * @param UsuarioDTO
     * @return lista de SelectItem <b>JSF<b/>
     */
    public List<SelectItem> retornaListaEmSelectItem(List<SetorDTO> setorDTOs) {
        List<SelectItem> items = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        CampusDTO campusDTO = null;
        for (int i = 0; i < setorDTOs.size(); i++) {
            if (campusDTO == null) {
                campusDTO = setorDTOs.get(i).getCampusDTO();
                builder.append(".. ");
                builder.append(setorDTOs.get(i).getCampusDTO().getDescricao());
                items.add(new SelectItem(42, builder.toString(), null, true, true, true));
                builder.delete(0, builder.length());
                items.add(new SelectItem(setorDTOs.get(i).getCodigo(), construtorSetor(setorDTOs.get(i), builder)));
                builder.delete(0, builder.length());
            } else {
                if (setorDTOs.get(i).getCampusDTO() != campusDTO) {
                    campusDTO = null;
                    i--;
                } else {
                    items.add(new SelectItem(setorDTOs.get(i).getCodigo(), construtorSetor(setorDTOs.get(i), builder)));
                    builder.delete(0, builder.length());
                }
            }

        }
        return items;
    }
    
    public List<SelectItem> listaTipoServicoSelectItem(List<TipoServicoDTO> tipoServicoDTOs) {
        List<SelectItem> items = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        SetorDTO setorDTO = null;
        for (int i = 0; i < tipoServicoDTOs.size(); i++) {
            if (setorDTO == null) {
                setorDTO = tipoServicoDTOs.get(i).getSetorDTO();
                builder.append(".. ");
                builder.append(tipoServicoDTOs.get(i).getSetorDTO().getDescricao());
                items.add(new SelectItem(42, builder.toString(), null, true, true, true));
                builder.delete(0, builder.length());
                items.add(new SelectItem(tipoServicoDTOs.get(i).getCodigo(), construtorTipoServico(tipoServicoDTOs.get(i), builder)));
                builder.delete(0, builder.length());
            } else {
                if (tipoServicoDTOs.get(i).getSetorDTO() != setorDTO) {
                    setorDTO = null;
                    i--;
                } else {
                    items.add(new SelectItem(tipoServicoDTOs.get(i).getCodigo(), construtorTipoServico(tipoServicoDTOs.get(i), builder)));
                    builder.delete(0, builder.length());
                }
            }

        }
        return items;
    }

    /**
     * Método auxiliar para montar os patrimonios
     *
     * @param patrimonioDTO
     * @param builder
     * @return builder.toString()
     */
    private String construtorPatrimonio(PatrimonioDTO patrimonioDTO, StringBuilder builder) {
        builder.append("...... ");
        builder.append(patrimonioDTO.getDescricao());
        return builder.toString();
    }

    /**
     * Método auxiliar para motar a localizacao
     *
     * @param localizacao
     * @param builder
     * @return builder.toString()
     */
    private String construtorLocalizacao(LocalizacaoDTO localizacao, StringBuilder builder) {
        builder.append(".... ");
        builder.append(localizacao.getDescricao());
        return builder.toString();
    }

    /**
     * retorna a concatenação do setor método auxiliar
     *
     * @param setorDTO
     * @param builder
     * @return
     */
    private String construtorSetor(SetorDTO setorDTO, StringBuilder builder) {
        builder.append("...... ");
        builder.append(setorDTO.getDescricao());
        return builder.toString();
    }
    
    private String construtorTipoServico(TipoServicoDTO tipoServicoDTO, StringBuilder builder) {
        builder.append("...... ");
        builder.append(tipoServicoDTO.getDescricao());
        return builder.toString();
    }
}