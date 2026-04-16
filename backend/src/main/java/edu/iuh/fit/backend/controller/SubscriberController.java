/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import edu.iuh.fit.backend.domain.Subscriber;
import edu.iuh.fit.backend.repository.SubscriberRepository;
import edu.iuh.fit.backend.service.SubscriberService;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import edu.iuh.fit.backend.util.error.InvalidException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;
    @PostMapping("/subscribers")
    @ApiMessage("Creste a subscriber")
    public ResponseEntity<Subscriber> create(@RequestBody Subscriber subscriber) throws InvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleCreate(subscriber));
    }
    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber){
        Subscriber subDB = this.subscriberService.findById(subscriber.getId());
        return ResponseEntity.ok().body(this.subscriberService.handleUpdate(subDB, subscriber));
    }

}
