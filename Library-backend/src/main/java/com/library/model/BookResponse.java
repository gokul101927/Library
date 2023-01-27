package com.library.model;

public record BookResponse(Long id, Long detailsId, String filename, String BookUri, String thumbnailUri) {
}
