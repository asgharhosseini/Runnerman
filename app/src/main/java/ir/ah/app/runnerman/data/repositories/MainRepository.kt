package ir.ah.app.runnerman.data.repositories

import ir.ah.app.runnerman.data.database.RunDAO
import ir.ah.app.runnerman.data.model.Run
import javax.inject.Inject

class MainRepository @Inject constructor(private val runDao: RunDAO) {
    suspend fun insertRun(run: Run) = runDao.insert(run)

    suspend fun deleteRun(run: Run) = runDao.delete(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
}