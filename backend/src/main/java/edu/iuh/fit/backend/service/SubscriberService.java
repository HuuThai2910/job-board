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

import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Subscriber;
import edu.iuh.fit.backend.dto.response.EmailJobResponse;
import edu.iuh.fit.backend.util.error.InvalidException;

public interface SubscriberService {
    Subscriber handleCreate(Subscriber subscriber) throws InvalidException;

    Subscriber handleUpdate(Subscriber subDB, Subscriber subsRequest);

    Subscriber findById(Long id);

    EmailJobResponse convertJobToSendEmail(Job job);

    void sendSubscribersEmailJobs();
}
