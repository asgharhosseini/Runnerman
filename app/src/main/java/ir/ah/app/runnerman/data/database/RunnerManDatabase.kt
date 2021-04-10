package ir.ah.app.runnerman.data.database

import androidx.room.Database
import androidx.room.TypeConverters
import ir.ah.app.runnerman.data.model.Run

@Database(entities = [Run::class], version = 1)
@TypeConverters(Converters::class)
abstract class RunnerManDatabase {
    abstract fun getRunDao():RunDAO
}