package br.com.physisbrasil.web.ephynances.model;

import java.io.Serializable;

public interface BaseModel extends Serializable {

    public static final String EMAIL_REGEX = ".+@.+\\.[a-z]+";

    Long getId();
}
