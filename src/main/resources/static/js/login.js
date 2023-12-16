var token = /*[[${session.token}]]*/ null;

// Guardar el token en el local storage
if (token) {
    localStorage.setItem('token', token);
}