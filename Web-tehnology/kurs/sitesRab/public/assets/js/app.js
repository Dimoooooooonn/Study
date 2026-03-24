(function () {
  const $ = (sel, root = document) => root.querySelector(sel);
  const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

  const y = $('#year');
  if (y) y.textContent = new Date().getFullYear();

  const burger = $('[data-burger]');
  const menu = $('[data-menu]');
  if (burger && menu) {
    burger.addEventListener('click', () => {
      const open = menu.classList.toggle('is-open');
      burger.setAttribute('aria-expanded', open ? 'true' : 'false');
    });
  }

  const openButtons = $$('[data-open-modal]');
  const closeButtons = $$('[data-close-modal]');
  const openModal = (id) => {
    const modal = document.getElementById(`modal-${id}`);
    if (!modal) return;
    modal.classList.add('is-open');
    modal.setAttribute('aria-hidden', 'false');
    document.body.style.overflow = 'hidden';
  };
  const closeModal = (modal) => {
    modal.classList.remove('is-open');
    modal.setAttribute('aria-hidden', 'true');
    document.body.style.overflow = '';
  };
  openButtons.forEach((btn) => btn.addEventListener('click', () => openModal(btn.dataset.openModal)));
  closeButtons.forEach((btn) => btn.addEventListener('click', () => {
    const modal = btn.closest('.modal');
    if (modal) closeModal(modal);
  }));

  const wizard = $('#bookingWizard');
  if (wizard) {
    const steps = $$('.step', wizard);
    const panels = $$('.wizard-panel', wizard);
    const nextBtns = $$('.js-next', wizard);
    const backBtns = $$('.js-back', wizard);
    let current = 0;

    const show = (index) => {
      current = Math.max(0, Math.min(index, panels.length - 1));
      steps.forEach((step, idx) => step.classList.toggle('is-active', idx === current));
      panels.forEach((panel, idx) => {
        panel.hidden = idx !== current;
      });
      wizard.scrollIntoView({ behavior: 'smooth', block: 'start' });
    };

    nextBtns.forEach((btn) => btn.addEventListener('click', () => show(current + 1)));
    backBtns.forEach((btn) => btn.addEventListener('click', () => show(current - 1)));
    show(0);
  }
})();
