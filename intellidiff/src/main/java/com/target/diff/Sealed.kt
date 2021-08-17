package com.target.diff

/**
 * Enforce exhaustive handling by treating when statements
 * like an expression
 *
 * ```
 * sealed class PrimaryColor {
 *   object Red: PrimaryColor()
 *   object Blue: PrimaryColor()
 *   object Green: PrimaryColor()
 * }
 *
 * // This will throw a compilation error
 * fun showColor(value: PrimaryColor) {
 *   when (value) {
 *     is Red -> ...
 *     is Blue -> ...
 *   }.exhaustive()
 * }
 *
 * // This will not
 * fun showColor(value: PrimaryColor) {
 *   when (value) {
 *     is Red -> ...
 *     is Blue -> ...
 *     is Green -> ...
 *   }.exhaustive()
 * }
 * ```
 *
 * Source: https://twitter.com/FMuntenescu/status/1044183533969969152
 */
@Suppress("unused", "NOTHING_TO_INLINE")
internal inline fun <T> T?.exhaustive() {
}
