/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ipsoftbrasil.web.agility.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Thadeu
 */
@Entity
@Table(name = "config")
public class Config implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", length = 1000, nullable = false)
    @Size(max = 1000)
    private String description;

    @Column(name = "property_name", length = 100, nullable = false, unique = true)
    @Size(max = 100)
    private String propertyName;

    @Column(name = "property_value", length = 1000, nullable = false)
    @Size(max = 1000)
    private String propertyValue;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
