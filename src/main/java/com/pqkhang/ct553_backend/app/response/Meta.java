package com.pqkhang.ct553_backend.app.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meta {
    int page;
    int pageSize;
    int pages;
    long total;
}
