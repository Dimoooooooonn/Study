(function () {
  const $ = (sel, root=document) => root.querySelector(sel);
  const $$ = (sel, root=document) => Array.from(root.querySelectorAll(sel));

  // year
  const y = $('#year');
  if (y) y.textContent = new Date().getFullYear();

  // mobile menu
  const burger = $('[data-burger]');
  const menu = $('[data-menu]');
  if (burger && menu) {
    burger.addEventListener('click', () => {
      const open = menu.classList.toggle('is-open');
      burger.setAttribute('aria-expanded', open ? 'true' : 'false');
    });
  }

  // simple modal system
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
  openButtons.forEach(btn => btn.addEventListener('click', () => openModal(btn.dataset.openModal)));
  closeButtons.forEach(btn => btn.addEventListener('click', () => {
    const modal = btn.closest('.modal');
    if (modal) closeModal(modal);
  }));

  // search page helper
  const searchForm = $('#searchForm');
  const searchInput = $('#searchInput');
  const results = $('#searchResults');
  if (searchForm && searchInput && results) {
    const params = new URLSearchParams(location.search);
    const q = params.get('q') || '';
    searchInput.value = q;

    const data = [
      {type:'Номер', title:'Стандарт, 2-местный', url:'rooms.html'},
      {type:'Путёвка', title:'Оздоровительная 7 дней', url:'packages.html'},
      {type:'Услуга', title:'Массаж спины', url:'services.html'},
      {type:'Страница', title:'О санатории', url:'about.html'},
      {type:'Страница', title:'Контакты', url:'contacts.html'},
      {type:'Расписание', title:'Йога для начинающих', url:'schedule.html'},
    ];

    const render = (query) => {
      results.innerHTML = '';
      const list = data.filter(x => (x.title + ' ' + x.type).toLowerCase().includes(query.toLowerCase()));
      if (!query.trim()) {
        results.innerHTML = '<p class="muted">Введите запрос для поиска по сайту.</p>';
        return;
      }
      if (!list.length) {
        results.innerHTML = '<p class="muted">Ничего не найдено. Попробуйте другой запрос.</p>';
        return;
      }
      const wrap = document.createElement('div');
      wrap.className = 'grid grid--3';
      list.forEach(x => {
        const c = document.createElement('a');
        c.className = 'card';
        c.href = x.url;
        c.innerHTML = `<div class="badge">${x.type}</div><h3 class="card__title" style="margin-top:10px">${x.title}</h3><div class="card__meta">Открыть</div>`;
        wrap.appendChild(c);
      });
      results.appendChild(wrap);
    };

    render(q);
    searchForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const query = searchInput.value.trim();
      const p = new URLSearchParams(location.search);
      p.set('q', query);
      history.replaceState(null, '', `${location.pathname}?${p.toString()}`);
      render(query);
    });
  }

  // booking wizard (UI only)
  const wizard = $('#bookingWizard');
  if (wizard) {
    const steps = $$('.step', wizard);
    const panels = $$('.wizard-panel', wizard);
    const nextBtns = $$('.js-next', wizard);
    const backBtns = $$('.js-back', wizard);
    let current = 0;

    const show = (i) => {
      current = Math.max(0, Math.min(i, panels.length - 1));
      steps.forEach((s, idx) => s.classList.toggle('is-active', idx === current));
      panels.forEach((p, idx) => p.hidden = idx !== current);
      wizard.scrollIntoView({behavior:'smooth', block:'start'});
    };
    nextBtns.forEach(btn => btn.addEventListener('click', () => show(current + 1)));
    backBtns.forEach(btn => btn.addEventListener('click', () => show(current - 1)));
    show(0);
  }
})();
