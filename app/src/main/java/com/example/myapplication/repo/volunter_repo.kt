package com.example.myapplication.repo

import com.example.myapplication.moduel.volunter_structure
import com.example.myapplication.volunter.ExcelReader
import java.io.InputStream

class volunter_repo {
  private val excelReader = ExcelReader()

  fun getStudentsFromExcel(inputStream: InputStream): Pair<List<volunter_structure>, String>  {
    return excelReader.readExcelFile(inputStream)
  }
}