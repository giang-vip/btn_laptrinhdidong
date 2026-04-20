package com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Translate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app_dich_quet_van_ban.data.model.LangItem
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslationViewModel : ViewModel() {

    // --- CÁC BIẾN QUẢN LÝ TRẠNG THÁI (STATE) ---
    var inputText by mutableStateOf("")
    var outputText by mutableStateOf("Kết quả dịch sẽ hiện ở đây...")
    var isTranslating by mutableStateOf(false)

    // Trạng thái hiển thị Menu chọn ngôn ngữ
    var showSourceMenu by mutableStateOf(false)
    var showTargetMenu by mutableStateOf(false)

    // Tên ngôn ngữ hiển thị trên giao diện
    var sourceLangName by mutableStateOf("English")
    var targetLangName by mutableStateOf("Vietnamese")

    // Mã ngôn ngữ thực tế để máy chạy (Logic nội bộ)
    private var sourceLangCode by mutableStateOf(TranslateLanguage.ENGLISH)
    private var targetLangCode by mutableStateOf(TranslateLanguage.VIETNAMESE)

    // Đối tượng bộ dịch (Biến này lưu giữ bộ máy dịch để dùng đi dùng lại cho nhanh)
    private var translator: Translator? = null

    // Danh sách các ngôn ngữ hỗ trợ (Bạn có thể thêm ở đây)
    val availableLanguages = listOf(
        LangItem("English", TranslateLanguage.ENGLISH),
        LangItem("Vietnamese", TranslateLanguage.VIETNAMESE),
        LangItem("French", TranslateLanguage.FRENCH),
        LangItem("Japanese", TranslateLanguage.JAPANESE),
        LangItem("Korean", TranslateLanguage.KOREAN)
    )

    // --- CHỨC NĂNG 1: CHUẨN BỊ BỘ DỊCH (TỐI ƯU TỐC ĐỘ) ---
    // Hàm này giúp tải trước model, khi bấm "Translate" sẽ có kết quả ngay lập tức
    fun prepareTranslator() {
        translator?.close() // Đóng bộ dịch cũ để tránh tốn RAM
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLangCode)
            .setTargetLanguage(targetLangCode)
            .build()
        translator = Translation.getClient(options)

        // Tải ngầm model ngôn ngữ (Cần internet lần đầu, các lần sau sẽ chạy Offline)
        translator?.downloadModelIfNeeded()
    }

    // --- CHỨC NĂNG 2: THỰC HIỆN DỊCH ---
    fun translate() {
        if (inputText.isBlank()) return
        isTranslating = true

        // Sử dụng bộ dịch đã chuẩn bị sẵn để dịch văn bản
        translator?.translate(inputText)
            ?.addOnSuccessListener { result ->
                outputText = result
                isTranslating = false // Tắt vòng xoay loading
            }
            ?.addOnFailureListener {
                outputText = "Lỗi: Vui lòng kiểm tra kết nối mạng lần đầu"
                isTranslating = false
            }
    }

    // --- CHỨC NĂNG 3: ĐỔI NGÔN NGỮ ---
    fun onSourceLangSelected(item: LangItem) {
        sourceLangName = item.name
        sourceLangCode = item.code
        prepareTranslator() // Sau khi đổi phải chuẩn bị lại bộ máy mới
    }

    fun onTargetLangSelected(item: LangItem) {
        targetLangName = item.name
        targetLangCode = item.code
        prepareTranslator() // Sau khi đổi phải chuẩn bị lại bộ máy mới
    }

    // Hàm hoán đổi nhanh 2 ngôn ngữ đang chọn
    fun swapLanguages() {
        val tempName = sourceLangName
        sourceLangName = targetLangName
        targetLangName = tempName

        val tempCode = sourceLangCode
        sourceLangCode = targetLangCode
        targetLangCode = tempCode

        prepareTranslator() // Cập nhật lại bộ máy dịch
    }
}