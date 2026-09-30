const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
            })
        });

        if (!response.ok) {
            document.getElementById('message').textContent = 'Đăng nhập thất bại';
            return;
        }

        const data = await response.json();
        localStorage.setItem('token', data.token);
        window.location.href = '/user/profile';
    });
}

const profile = document.getElementById('profile');
if (profile) {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login';
    } else {
        fetch('/users/me', {
            headers: {'Authorization': 'Bearer ' + token}
        })
            .then(async response => {
                if (!response.ok) throw new Error('Unauthorized');
                return response.json();
            })
            .then(data => profile.textContent = JSON.stringify(data, null, 2))
            .catch(() => {
                localStorage.removeItem('token');
                window.location.href = '/login';
            });
    }
}

const logout = document.getElementById('logout');
if (logout) {
    logout.addEventListener('click', () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    });
}
