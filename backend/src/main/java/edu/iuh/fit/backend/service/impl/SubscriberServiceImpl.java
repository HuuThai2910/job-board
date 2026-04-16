/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.domain.Subscriber;
import edu.iuh.fit.backend.dto.response.EmailJobResponse;
import edu.iuh.fit.backend.repository.JobRepository;
import edu.iuh.fit.backend.repository.SkillRepository;
import edu.iuh.fit.backend.repository.SubscriberRepository;
import edu.iuh.fit.backend.service.EmailService;
import edu.iuh.fit.backend.util.error.InvalidException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class SubscriberServiceImpl implements edu.iuh.fit.backend.service.SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    @Override
    public Subscriber handleCreate(Subscriber subscriber) throws InvalidException {
        boolean isExists = this.subscriberRepository.existsByEmail(subscriber.getEmail());
        if(isExists == true){

                throw new InvalidException("Email " +subscriber.getEmail() + "exists");

        }
        if(subscriber.getSkills() != null){
            List<Long> reqSkills = subscriber.getSkills()
                    .stream().map(Skill::getId)
                    .toList();
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }
    @Override
    public Subscriber handleUpdate(Subscriber subDB, Subscriber subsRequest){
        if(subsRequest.getSkills() != null){
            List<Long> reqSkills = subsRequest.getSkills()
                    .stream().map(Skill::getId)
                    .toList();
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subDB);
    }

    @Override
    public Subscriber findById(Long id){
        return this.subscriberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Subscriber not found"));
    }
    @Override
    public EmailJobResponse convertJobToSendEmail(Job job) {
        EmailJobResponse res = new EmailJobResponse();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new EmailJobResponse.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<EmailJobResponse.SkillEmail> s = skills.stream().map(skill -> new EmailJobResponse.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }


    @Override
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (!listSubs.isEmpty()) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && !listSkills.isEmpty()) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && !listJobs.isEmpty()) {

                         List<EmailJobResponse> arr = listJobs.stream().map(
                         job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }


}
