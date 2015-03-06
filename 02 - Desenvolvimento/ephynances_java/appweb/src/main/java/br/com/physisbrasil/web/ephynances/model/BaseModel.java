package br.com.physisbrasil.web.ephynances.model;

import java.io.Serializable;

public interface BaseModel extends Serializable {

    public static final String EMAIL_REGEX = ".+@.+\\.[a-z]+";
    public static final String CPF_REGEX = "/^d{3}.d{3}.d{3}-d{2}$/";
    public static final String CNPJ_REGEX = "/^d{2}.d{3}.d{3}/d{4}-d{2}$/";

    Long getId();
}
