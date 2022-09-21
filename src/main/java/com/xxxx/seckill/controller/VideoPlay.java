package com.xxxx.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/video")
public class VideoPlay {
    @RequestMapping("/play")
    public String paly(Model model){
        model.addAttribute("address","img/test.mp4");
        return "Video";
    }
}
