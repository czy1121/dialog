# dialog

AlertDialog扩展(tip/alert/confirm/choose) 和 自定义对话框(CustomDialog/BottomDialog/DropdownDialog/SideDialog)

AlertDialog扩展

- tip 显示没有按钮的 AlertDialog
- alert 显示一个按钮(ok)的 AlertDialog
- confirm 显示两个按钮(ok/cancel)的 AlertDialog
- choose 弹出一个单选列表

自定义对话框

- CustomDialog - 自定义对话框基类，封装了众多实用功能
  - setLayout 简化设置 Window 的宽高
  - setDimAmount 简化设置背景的不透明遮罩
  - setButton 简化按钮事件设置
  - setView 取代 setContentView 并持有传入的 View
  - requireView 获取 setView 设置 View
  - setAnimation 支持通过代码设置 进场/离场 动画
  - dismiss 重载使离场时默认播放离场动画
  - dismissImmediately 为不播放动画立即关闭对话框
  - 实现 LifecycleOwner 支持生命周期
- BottomDialog - 在屏幕底部弹出的对话框
- DropdownDialog - 在指定View的下面弹出对话框
- SideDialog - 在屏幕左边或右边弹出的对话框

一些简单的对话框

- LoadingDialog - 在屏幕中央显示一个加载动画
- InputDialog - 在弹框中输入一个文本并返回
- ActionSheetDialog
- PickerDialog

## Usage

## Gradle

``` groovy
repositories {
    maven { url "https://gitee.com/ezy/repo/raw/cosmo/"}
}
dependencies {
    // AlertDialog扩展(tip/alert/confirm/choose)
    implementation "me.reezy.cosmo:dialog-alert:0.7.0"

    // 自定义对话框(CustomDialog/BottomDialog/DropdownDialog/SideDialog)
    implementation "me.reezy.cosmo:dialog-custom:0.7.0"

    // 一些简单的对话框(LoadingDialog/InputDialog/ActionSheetDialog/PickerDialog)
    implementation "me.reezy.cosmo:dialog-simple:0.7.0"
}
```


## LICENSE

The Component is open-sourced software licensed under the [Apache license](LICENSE).