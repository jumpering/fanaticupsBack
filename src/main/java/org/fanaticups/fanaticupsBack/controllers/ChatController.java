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
        List<MessageEntity> messageEntityList = this.chatService.findAllMessages(cupId).get();
        return messageEntityList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(messageEntityList);
    }
}
