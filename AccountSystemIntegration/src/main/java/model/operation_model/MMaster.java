/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chama
 */
@Entity
@Table(name = "m_master")
public class MMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "index_no")
    private Integer indexNo;

    @Column(name = "name")
    private String name;

    @Column(name = "enter_date")
    private String enterDate;

    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "type")
    private String type;

    @Column(name = "type_index")
    private String typeIndex;

    @Column(name = "branch")
    private Integer branch;

    @Column(name = "other1")
    private String other1;

    @Column(name = "other2")
    private String other2;

    @Column(name = "other3")
    private String other3;

    @Column(name = "check")
    private boolean check;

    public MMaster() {
    }

    public MMaster(Integer indexNo, String name, String enterDate, String updatedDate, String type, String typeIndex, Integer branch, String other1, String other2, String other3, boolean check) {
        this.indexNo = indexNo;
        this.name = name;
        this.enterDate = enterDate;
        this.updatedDate = updatedDate;
        this.type = type;
        this.typeIndex = typeIndex;
        this.branch = branch;
        this.other1 = other1;
        this.other2 = other2;
        this.other3 = other3;
        this.check = check;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(String typeIndex) {
        this.typeIndex = typeIndex;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getOther3() {
        return other3;
    }

    public void setOther3(String other3) {
        this.other3 = other3;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
