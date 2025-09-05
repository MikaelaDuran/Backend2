document.addEventListener('DOMContentLoaded', () => {
    const openBtn = document.getElementById('openModal');
    const modal = document.getElementById('modal');
    const usernameInput = document.getElementById('username');
    let isLoggedIn = false; // track if logged in

    if (!openBtn || !modal) {
        console.warn('Login button or modal not found.');
        return;
    }


    function closeModal() {
        modal.classList.remove('open');
        modal.setAttribute('aria-hidden', 'true');
        openBtn.setAttribute('aria-expanded', 'false');
        document.body.style.overflow = '';
        openBtn.focus();
    }

    function openModal() {
        if (isLoggedIn) {
            // LOG OUT
            isLoggedIn = false;
            openBtn.textContent = 'Login';

            //Remove after
            alert('You have been logged out!');
            return;
        }
        // Normal open modal behavior
        modal.classList.add('open');
        modal.setAttribute('aria-hidden', 'false');
        openBtn.setAttribute('aria-expanded', 'true');
        setTimeout(() => usernameInput?.focus(), 50);
        document.body.style.overflow = 'hidden';
    }

    // ✅ event listeners belong here, not inside openModal
    openBtn.addEventListener('click', openModal);

    modal.addEventListener('click', (e) => {
        if (e.target.hasAttribute('data-close')) closeModal();
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal.classList.contains('open')) closeModal();
    });

    document.getElementById('loginForm')?.addEventListener('submit', (e) => {
        e.preventDefault();

        // ADD LOGIC TO ACTUALLY LOG IN
        isLoggedIn = true;
        openBtn.textContent = 'Logout'; // change button text

        //Remove after
        alert('Logged in! (demo)');

        closeModal();
    });

    document.getElementById('createAccount')?.addEventListener('click', () => {
        window.location.href = "register.html";
    });
});
