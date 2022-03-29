package com.febrian.icc.data.source.local

class LocalDataSource(private val dao: NewsDao) : NewsDao {
    companion object {
        @Volatile
        private var instance: LocalDataSource? = null
        fun getInstance(dao: NewsDao): LocalDataSource {
            return instance ?: synchronized(this) {
                instance ?: LocalDataSource(dao).apply { instance = this }
            }
        }
    }

    override fun insert(news: EntityNews) {
        dao.insert(news)
    }

    override fun delete(title: String) {
        dao.delete(title)
    }

    override fun getAllNews(): List<EntityNews> {
        return dao.getAllNews()
    }

    override fun getNewsById(id: Int): EntityNews {
        return dao.getNewsById(id)
    }

    override fun getNewsByTitle(title: String): EntityNews {
        return dao.getNewsByTitle(title)
    }

    override fun newsExist(title: String): Boolean {
        return dao.newsExist(title)
    }

}