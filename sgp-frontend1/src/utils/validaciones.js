// Utilidades de validación de formularios

// Verifica que un campo esté vacío
export const campoVacio = (valor) => {
  if (valor === undefined || valor === null) return true
  return String(valor).trim() === ''
}

// Verifica que un texto contenga solo números enteros
export const soloNumeros = (valor) => {
  return /^[0-9]+$/.test(String(valor).trim())
}

// Verifica que un texto sea un número (enteros o decimales)
export const esNumeroValido = (valor) => {
  return /^[0-9]+(\.[0-9]+)?$/.test(String(valor).trim())
}

// Verifica que un correo electrónico tenga un formato válido
export const correoValido = (correo) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(correo).trim())
}

// Retorna los nombres de los campos obligatorios que están vacíos
// Recibe el objeto del formulario y una lista con los nombres de campo
export const camposObligatoriosVacios = (formulario, campos) => {
  return campos.filter((campo) => campoVacio(formulario[campo]))
}