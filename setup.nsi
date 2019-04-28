; example2.nsi

;

; This script is based on example1.nsi, but it remember the directory, 

; has uninstall support and (optionally) installs start menu shortcuts.

;

; It will install example2.nsi into a directory that the user selects,



;--------------------------------


; The name of the installer

Name "PC-Control installer"



; The file to write

OutFile "target\PC-Control-Installer.exe"

; The default installation directory

InstallDir $PROFILE\PC-Control



; Registry key to check for directory (so if you install again, it will

; overwrite the old one automatically)

InstallDirRegKey HKCU "Software\PC-Control" "Install_Dir"



RequestExecutionLevel user



!define SRC_FILE "target\PC-Control.exe"
!define SRC_FILE2 "src\main\resources\volumeControl.exe"
!define PROGRAM_NAME "PC-Control.exe"



;--------------------------------



; Pages



Icon "src\main\resources\icon.ico"
UninstallIcon "src\main\resources\icon.ico"

Page components

Page directory

Page instfiles



UninstPage uninstConfirm

UninstPage instfiles

 

!macro terminateService

   StrCpy $0 ${PROGRAM_NAME}


   DetailPrint "Searching for processes called '$0'"

   KillProc::FindProcesses

   StrCmp $1 "-1" wooops

   DetailPrint "-> Found $0 processes"


 
  StrCmp $0 "0" completed

   Sleep 1500


 
  StrCpy $0 ${SRC_FILE}

   DetailPrint "Killing all processes called '$0'"

   KillProc::KillProcesses

   StrCmp $1 "-1" wooops

   DetailPrint "-> Killed $0 processes, failed to kill $1 processes"


 
  Goto completed



   wooops:

   DetailPrint "-> Error: Something went wrong."

   Abort



   completed:
   DetailPrint "Everything went okay :-D"

 !macroend



;--------------------------------



; The stuff to install

Section PC-Control server"

  !insertmacro terminateService


  SectionIn RO



  ; Set output path to the installation directory.

  SetOutPath $INSTDIR


  ; Put file there

  File ${SRC_FILE}
  File ${SRC_FILE2}


  ; Write the installation path into the registry

  WriteRegStr HKCU SOFTWARE\PC-Control "Install_Dir" "$INSTDIR"


  ; Write the uninstall keys for Windows

  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\PC-Control" "DisplayName" "PC-Control"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\PC-Control" "UninstallString" '"$INSTDIR\uninstall.exe"'

  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\PC-Control" "NoModify" 1

  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\PC-Control" "NoRepair" 1

  WriteUninstaller "uninstall.exe"



  ; Auto start
  
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Run" "PC-Control" "$INSTDIR\${SRC_FILE}.exe"


SectionEnd



; Optional section (can be disabled by the user)


Section "Start Menu Shortcuts"


  CreateDirectory "$SMPROGRAMS\PC-Control"

  CreateShortcut "$SMPROGRAMS\PC-Control\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0

  CreateShortcut "$SMPROGRAMS\PC-Control\AdBot.lnk" "$INSTDIR\${PROGRAM_NAME}" "" "$INSTDIR\${PROGRAM_NAME}" 0


  CreateShortCut "$DESKTOP\PC-Control.lnk" "$INSTDIR\${PROGRAM_NAME}" ""

  Exec '"$WINDIR\explorer.exe" "$INSTDIR"'

SectionEnd

;--------------------------------



; Uninstaller



Section "Uninstall"

  !insertmacro terminateService


 
 ; Remove registry keys

  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\PC-Control"

  DeleteRegKey HKCU SOFTWARE\PC-Control

  DeleteRegValue HKCU "Software\Microsoft\Windows\CurrentVersion\Run" "PC-Control"



  ; Remove files and uninstaller

  Delete $INSTDIR\${PROGRAM_NAME}
  Delete $INSTDIR\uninstall.exe
  Delete $INSTDIR\volumeControl.exe
  Delete $INSTDIR\PC-Control.log
  Delete $DESKTOP\PC-Control.lnk


  ; Remove shortcuts, if any

  Delete "$SMPROGRAMS\PC-Control\*.*"



  ; Remove directories used

  RMDir "$SMPROGRAMS\PC-Control"

  RMDir "$INSTDIR"


SectionEnd
