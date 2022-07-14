package com.esgi.framework_JEE.slot.web.controller;


import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.web.request.SlotRequest;
import com.esgi.framework_JEE.slot.web.response.SlotResponse;
import com.esgi.framework_JEE.slot.command.SlotCommand;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.slot.query.SlotQuery;
import com.esgi.framework_JEE.user.query.UserQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/slot")
public class SlotController {
    final
    SlotCommand slotCommand;

    private final
    SlotQuery slotQuery;
    private final UserQuery userQuery;

    /*
    todo
        get by day maybe ? a voir plus tard
     */

    public SlotController(SlotCommand slotCommand, SlotQuery slotQuery, UserQuery userQuery) {
        this.slotCommand = slotCommand;
        this.slotQuery = slotQuery;
        this.userQuery = userQuery;
    }

    
    @PostMapping("/create")
    public ResponseEntity<SlotResponse> create(@RequestBody SlotRequest slotRequest) throws ParseException {
        var user = userQuery.getById(slotRequest.user_id);
        var slot = slotCommand.create(slotRequest, user);
        if(slot == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(slotToSlotResponse(slot),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SlotResponse>> getAll(){
        return new ResponseEntity<>(
                listSlotToListSlotResponse(
                        slotQuery.getAll()
                ),
                HttpStatus.OK);
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<SlotResponse> getSlotById(@PathVariable int slotId){
        var slot = slotQuery.getById(slotId);
        if(slot == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(
                slotToSlotResponse(slot),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SlotResponse>> getSlotByUser(@PathVariable int userId){
        return new ResponseEntity<>(
                listSlotToListSlotResponse(
                        slotQuery.findAllByUser(
                                userQuery.getById(userId)
                        )
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/start")
    public ResponseEntity<List<SlotResponse>> getByStart(@RequestBody SlotRequest slotRequest) throws ParseException {
        var slot = slotQuery.findByStartSlot(slotRequest.start);
        return new ResponseEntity<>(
                listSlotToListSlotResponse(slot),
                HttpStatus.OK
        );
    }


    @GetMapping("/interval")
    public ResponseEntity<List<SlotResponse>> getByIntervalAtStart(@RequestBody SlotRequest slotRequest) throws ParseException {
        var slot = slotQuery.findSlotsByInterval(slotRequest.start, slotRequest.end);
        return new ResponseEntity<>(
                listSlotToListSlotResponse(slot),
                HttpStatus.OK
        );
    }

    @PatchMapping("/start/{slotId}")
    public ResponseEntity<SlotResponse> changeStart(@PathVariable int slotId,@RequestBody SlotRequest slotRequest) throws ParseException {
        var slot = slotCommand.changeStart(slotId,slotRequest);
        if(slot == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(slotToSlotResponse(slot), HttpStatus.OK);
    }

    @PatchMapping("/end/{slotId}")
    public ResponseEntity<SlotResponse> changeEnd(@PathVariable int slotId,@RequestBody SlotRequest slotRequest) throws ParseException {
        var slot = slotCommand.changeEnd(slotId,slotRequest);
        if(slot == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(slotToSlotResponse(slot), HttpStatus.OK);
    }


    @DeleteMapping("/{slotId}")
    public ResponseEntity<?> deleteSlot(@PathVariable int slotId){
        var slot = slotQuery.getById(slotId);
        if(slot == null)
            return new ResponseEntity<>(
                    "Slot " + slotId + " not exist",
                    HttpStatus.BAD_REQUEST
            );
        slotCommand.delete(slotId);
        return new ResponseEntity<>(
                "Slot " + slotId + " deleted",
                HttpStatus.OK
        );
    }







    private SlotResponse slotToSlotResponse(Slot slot){
        return new SlotResponse()
                .setId(slot.getId())
                .setStart(DateManipulator.dateToString(slot.getStartSlot()))
                .setEnd(DateManipulator.dateToString(slot.getEndSlot()));
    }

    private List<SlotResponse> listSlotToListSlotResponse(List<Slot> slots){
        List<SlotResponse> slotResponses = new ArrayList<>();
        slots.forEach(slot -> slotResponses.add(
                slotToSlotResponse(slot))
        );
        return slotResponses;
    }
}
