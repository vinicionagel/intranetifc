package com.dto;

import java.util.List;  
import javax.faces.model.ListDataModel;   
import org.primefaces.model.SelectableDataModel;  
  
public class ChamadoInfraModel extends ListDataModel<ChamadoInfraDTO> implements SelectableDataModel<ChamadoInfraDTO> {    
  
    public ChamadoInfraModel() {  
    }  
  
    public ChamadoInfraModel(List<ChamadoInfraDTO> data) {  
        super(data);  
    }  
      
    @Override  
    public ChamadoInfraDTO getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<ChamadoInfraDTO> chamadoInfraDTOs = (List<ChamadoInfraDTO>) getWrappedData();  
          
        for(ChamadoInfraDTO chamadoInfraDTO : chamadoInfraDTOs) {  
            if(chamadoInfraDTO.getTitulo().equals(rowKey))  
                return chamadoInfraDTO;  
        }  
          
        return null;  
    }  
    
  
    @Override  
    public Object getRowKey(ChamadoInfraDTO object) {  
        return object.getCodigo();  
    }  
}  
        