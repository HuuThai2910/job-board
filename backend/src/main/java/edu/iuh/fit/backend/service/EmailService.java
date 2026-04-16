/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service;/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */

public interface EmailService {

    void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);


    void sendEmailFromTemplateSync(String to, String subject,
                                   String templateName, String userName, Object value);
}
