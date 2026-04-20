import com.example.app_dich_quet_van_ban.data.local.dao.ScanDao
import com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity
import com.example.app_dich_quet_van_ban.domain.model.TranslationResult
import com.example.app_dich_quet_van_ban.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow

class TranslationRepositoryImpl(private val scanDao: ScanDao) : TranslationRepository {
    // ... các hàm cũ giữ nguyên ...

    suspend fun insertDoc(doc: ScannedDocEntity) {
        scanDao.insertDoc(doc)
    }

    override suspend fun translateText(
        text: String,
        source: String,
        target: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveToHistory(result: TranslationResult) {
        TODO("Not yet implemented")
    }

    override fun getHistory(): Flow<List<TranslationResult>> {
        TODO("Not yet implemented")
    }

    // THÊM HÀM NÀY:
    override suspend fun getDocByTitle(fileName: String): ScannedDocEntity? {
        return scanDao.getDocByTitle(fileName)
    }

    fun getAllDocs(): Flow<List<ScannedDocEntity>> {
        return scanDao.getAllScannedDocs()
    }

    override fun searchDocs(query: String): Flow<List<ScannedDocEntity>> {
        return scanDao.searchDocs(query)
    }

    override fun updateDoc(updatedDoc: ScannedDocEntity) {
        TODO("Not yet implemented")
    }
}