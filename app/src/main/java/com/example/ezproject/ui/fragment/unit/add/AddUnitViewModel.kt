package com.example.ezproject.ui.fragment.unit.add

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.network.response.GovernmentResponse
import com.example.ezproject.data.network.response.ImageUploadResponse
import com.example.ezproject.data.network.response.SaveUnitDraftResponse
import com.example.ezproject.data.network.response.UnitDraftResponse
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable
import java.io.File

class AddUnitViewModel(val unitRepo: UnitRepo, val userRepo: UserRepo) : ViewModel() {
    var unitId = 0
    var unitDraft: UnitDraftResponse? = null
    val currency: String by lazy { userRepo.currentCurrency() }

    fun loadUnitDraft(): Observable<UnitDraftResponse> {
        return unitRepo.unitDraft(unitId)
    }

    fun saveDraft(): Observable<SaveUnitDraftResponse> = unitRepo.saveUnitDraft(unitDraft?.unit)

    fun governments(parentId: String): Observable<GovernmentResponse> {
        return unitRepo.goverments(parentId)
    }

    fun areas(parentId: String): Observable<GovernmentResponse> {
        return unitRepo.areas(parentId)
    }


    fun cities(parentId: String): Observable<GovernmentResponse> {
        return unitRepo.cities(parentId)
    }

    fun uploadImage(image: File): Observable<ImageUploadResponse> {
        return unitRepo.uploadImage(image)
    }

    fun uploadFile(file: File): Observable<ImageUploadResponse> {
        return unitRepo.uploadFile(file)
    }
}

