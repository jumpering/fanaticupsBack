package org.fanaticups.fanaticupsBack.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.fanaticups.fanaticupsBack.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Operation(summary = "Get all chat messages from cup ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found cups",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ChatEntity.class))}
            )})
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/chat/{cupId}")
    public ResponseEntity<List<MessageEntity>> findAllMessagesByCupId(@PathVariable Long cupId) {
        List<MessageEntity> messageEntityList = this.chatService.findAllMessages(cupId);
        return ResponseEntity.ok(messageEntityList);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/chat")
    public ResponseEntity<MessageEntity> addMessage(@RequestParam("cupId") Long cupId,
                                              @RequestParam("userId") Long userId,
                                              @RequestParam("message") String message){
        Optional<MessageEntity> optionalMessage = this.chatService.createMessage(cupId, userId, message);
        return optionalMessage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());

    }
}
