# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\kirsik_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\kirsik_autogen.dir\\ParseCache.txt"
  "kirsik_autogen"
  )
endif()
