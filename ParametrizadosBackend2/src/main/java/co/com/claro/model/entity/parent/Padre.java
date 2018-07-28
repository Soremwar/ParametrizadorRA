/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity.parent;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author andresbedoya
 */
@MappedSuperclass

public abstract class Padre<T> {
    @Id
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public abstract T toDTO();  

}
