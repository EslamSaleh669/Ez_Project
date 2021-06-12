package com.example.ezproject.data.models

data class SearchPlaces(val id: Int, val name: String) {
    override fun toString(): String {
        return this.name
    }
}