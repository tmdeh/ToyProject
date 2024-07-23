package com.fc.toyproeject2.domain.discord.controller;

import com.fc.toyproeject2.domain.discord.service.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiscordController {

  private final DiscordService discordService;

  @GetMapping("/discord")
  public ResponseEntity<?> discord(@RequestParam(name = "tripId") Long tripId) {
    discordService.discord(tripId);
    return ResponseEntity.ok().build();
  }

}