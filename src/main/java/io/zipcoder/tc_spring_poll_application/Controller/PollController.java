package io.zipcoder.tc_spring_poll_application.Controller;

import io.zipcoder.tc_spring_poll_application.Domain.Poll;
import io.zipcoder.tc_spring_poll_application.Exception.ResourceNotFoundException;
import io.zipcoder.tc_spring_poll_application.Repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PollController {

    PollRepository pollRepository;

    @Autowired
    PollController(PollRepository pollRepository){
        this.pollRepository = pollRepository;
    }

    @RequestMapping(value="/polls", method= RequestMethod.GET)
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @RequestMapping(value="/polls", method=RequestMethod.POST)
    public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        poll = pollRepository.save(poll);

        URI newPollUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(poll.getId())
                .toUri();

        return new ResponseEntity<>(newPollUri, HttpStatus.CREATED);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.GET)
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
        Poll p = pollRepository.findOne(pollId);
        return new ResponseEntity<> (p, HttpStatus.OK);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.PUT)
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        // Save the entity
        Poll p = pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        pollRepository.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyPoll(Long pollId) {
        if(!pollRepository.exists(pollId)) {
            throw new ResourceNotFoundException("Not available.");
        }
        }

}
