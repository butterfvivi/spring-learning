package org.vivi.framework.ai.simple.controller;

import org.springframework.ai.image.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对话模型 chat Model
 * 接收用户文本输入，并将模型生成的图片作为输出返回
 *
 */
@RestController
public class ImageModelController {
    private final ImageModel imageModel;

    ImageModelController(@Qualifier("dashScopeImageModel") ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @RequestMapping("/image")
    public String image(String input) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .model("wanx2.1-t2i-turbo")
                .height(1024)
                .width(1024)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(input, options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        return "redirect:" + imageUrl;
    }
}