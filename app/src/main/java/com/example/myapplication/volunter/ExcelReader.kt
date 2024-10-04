package com.example.myapplication.volunter

import android.util.Log
import com.example.myapplication.moduel.volunter_structure
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row

class ExcelReader {

  fun readExcelFile(inputStream: InputStream): Pair<List<volunter_structure>, String> {
    val students = mutableListOf<volunter_structure>()
    val workbook = WorkbookFactory.create(inputStream)
    val sheet = workbook.getSheetAt(0) // Assuming data is in the first sheet
    val formatter = DataFormatter() //Create a DataFormatter
    val headerRow = sheet.getRow(0)
    val mismatchedHeaders = mutableMapOf<String, String>()


    if (headerRow != null) {
      val requiredHeaders = listOf("instCode", "instName", "district", "enrollmentNo", "studentname", "gender", "dateOfBirth", "cast", "branch", "mainSubject", "email", "phoneNo")

      for (header in requiredHeaders) {
        val headerIndex = findHeaderIndex(headerRow, header)
        if (headerIndex == -1) {
          val excelHeader = headerRow.getCell(requiredHeaders.indexOf(header))?.stringCellValue ?: ""
          mismatchedHeaders[excelHeader] = header
        }
      }
      if (mismatchedHeaders.isNotEmpty()) {
        val errorMessage = getErrorMessageForMismatchedHeaders(mismatchedHeaders)
        return Pair(emptyList(), errorMessage)
      }

      for (i in 1 until sheet.physicalNumberOfRows) {
        val row = sheet.getRow(i)
        if (row != null) {
          val instCode = getCellValue(row, headerRow, "instCode", formatter)
          val instName = getCellValue(row, headerRow, "instName", formatter)
          val district = getCellValue(row, headerRow, "district", formatter)
          val enrollmentNo = getCellValue(row, headerRow, "enrollmentNo", formatter)
          val studentname = getCellValue(row, headerRow, "studentname", formatter)
          val gender = getCellValue(row, headerRow, "gender", formatter)
          val dateOfBirth = getCellValue(row, headerRow, "dateOfBirth", formatter)
          val cast = getCellValue(row, headerRow, "cast", formatter)
          val branch = getCellValue(row, headerRow, "branch", formatter)
          val mainSubject = getCellValue(row, headerRow, "mainSubject", formatter)
          val email = getCellValue(row, headerRow, "email", formatter)
          val phoneNo = getCellValue(row, headerRow, "phoneNo", formatter)

          val student = volunter_structure(
            instCode, instName, district, enrollmentNo, studentname, gender, dateOfBirth,
            cast, branch, mainSubject, email, phoneNo
          )
          students.add(student)
        }
      }
    }
    else {
      return Pair(emptyList(), "Error: No header row found in the Excel file.")
    }

    return Pair(students, "")
  }

 private fun getCellValue(row: Row, headerRow: Row, header: String, formatter: DataFormatter): String {
    val headerIndex = findHeaderIndex(headerRow, header)
    if (headerIndex != -1) {
      val cell = row.getCell(headerIndex)
      return formatter.formatCellValue(cell)
    } else {
      Log.w("ExcelReader", "Header '$header' not found!")
      return "" // Or set a default value
    }
  }

  private fun findHeaderIndex(headerRow: org.apache.poi.ss.usermodel.Row, header: String): Int {
    for (i in 0 until headerRow.physicalNumberOfCells) {
      val cell = headerRow.getCell(i)
      if (cell != null && cell.cellType == CellType.STRING && cell.stringCellValue.trim() == header) {
        return i
      }
    }
    return -1
  }

  private fun getErrorMessageForMismatchedHeaders(mismatchedHeaders: Map<String, String>): String {
    if (mismatchedHeaders.isEmpty()) {
      return ""

    }
    val errorMessage = StringBuilder("Error: The following headers are mismatched in the Excel file:\n")
    for ((excelHeader, readerHeader) in mismatchedHeaders) {
      errorMessage.append("'$excelHeader' should be '$readerHeader'\n")
    }
    return errorMessage.toString()
  }

}