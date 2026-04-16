/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
}
