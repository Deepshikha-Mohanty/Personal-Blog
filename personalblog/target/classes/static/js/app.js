/* =========================================================
   Personal Blog — shared front-end behaviour (vanilla JS)
   ========================================================= */

(function () {
  "use strict";

  /* ---------- Theme (light / dark) ---------- */
  const THEME_KEY = "blog-theme";

  function applyTheme(theme) {
    document.documentElement.setAttribute("data-theme", theme);
    document.querySelectorAll("[data-theme-toggle]").forEach((btn) => {
      btn.innerHTML = theme === "dark" ? sunIcon() : moonIcon();
      btn.setAttribute("aria-label", theme === "dark" ? "Switch to light mode" : "Switch to dark mode");
    });
  }

  function initTheme() {
    const saved = localStorage.getItem(THEME_KEY);
    const prefersDark = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
    applyTheme(saved || (prefersDark ? "dark" : "light"));

    document.querySelectorAll("[data-theme-toggle]").forEach((btn) => {
      btn.addEventListener("click", () => {
        const current = document.documentElement.getAttribute("data-theme");
        const next = current === "dark" ? "light" : "dark";
        localStorage.setItem(THEME_KEY, next);
        applyTheme(next);
      });
    });
  }

  function sunIcon() {
    return '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4.9 4.9l1.4 1.4M17.7 17.7l1.4 1.4M2 12h2M20 12h2M4.9 19.1l1.4-1.4M17.7 6.3l1.4-1.4"/></svg>';
  }
  function moonIcon() {
    return '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>';
  }

  /* ---------- Password show/hide ---------- */
  function initPasswordToggle() {
    document.querySelectorAll("[data-toggle-password]").forEach((btn) => {
      btn.addEventListener("click", () => {
        const targetId = btn.getAttribute("data-toggle-password");
        const input = document.getElementById(targetId);
        if (!input) return;
        const isHidden = input.type === "password";
        input.type = isHidden ? "text" : "password";
        btn.textContent = isHidden ? "HIDE" : "SHOW";
      });
    });
  }

  /* ---------- Character counters ---------- */
  function initCharCounters() {
    document.querySelectorAll("[data-counter-for]").forEach((counter) => {
      const inputId = counter.getAttribute("data-counter-for");
      const max = parseInt(counter.getAttribute("data-max"), 10) || null;
      const input = document.getElementById(inputId);
      if (!input) return;

      const update = () => {
        const len = input.value.length;
        counter.textContent = max ? `${len} / ${max}` : `${len} characters`;
      };
      input.addEventListener("input", update);
      update();
    });
  }

  /* ---------- Genre colour tagging ---------- */
  const GENRE_PALETTE = {
    technology: "#6C5CE7",
    programming: "#0984e3",
    travel: "#00b894",
    education: "#e17055",
    food: "#e84393",
    lifestyle: "#fdcb6e",
    sports: "#00cec9",
    health: "#ff6b6b",
    other: "#7c7f92"
  };

  function hashColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
    const hue = Math.abs(hash) % 360;
    return `hsl(${hue}, 65%, 55%)`;
  }

  function colorizeGenreTags() {
    document.querySelectorAll("[data-genre]").forEach((el) => {
      const genre = (el.getAttribute("data-genre") || "").trim();
      const key = genre.toLowerCase();
      const color = GENRE_PALETTE[key] || hashColor(genre || "default");
      el.style.setProperty("--genre-color", color);
      const card = el.closest(".blog-card");
      if (card) card.style.setProperty("--genre-color", color);
    });
  }

  /* ---------- Custom delete-confirm modal (replaces window.confirm) ---------- */
  function initConfirmModal() {
    const forms = document.querySelectorAll("form[data-confirm]");
    if (!forms.length) return;

    let overlay = document.getElementById("confirmModal");
    if (!overlay) {
      overlay = document.createElement("div");
      overlay.id = "confirmModal";
      overlay.className = "modal-overlay";
      overlay.innerHTML = `
        <div class="modal-box">
          <div class="icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="3 6 5 6 21 6"></polyline>
              <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"></path>
              <path d="M10 11v6M14 11v6M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"></path>
            </svg>
          </div>
          <h3>Delete this post?</h3>
          <p id="confirmModalMsg">This action cannot be undone.</p>
          <div class="modal-actions">
            <button type="button" class="btn btn-ghost" id="confirmModalCancel">Cancel</button>
            <button type="button" class="btn btn-danger" id="confirmModalOk">Delete</button>
          </div>
        </div>`;
      document.body.appendChild(overlay);
    }

    const msgEl = document.getElementById("confirmModalMsg");
    const okBtn = document.getElementById("confirmModalOk");
    const cancelBtn = document.getElementById("confirmModalCancel");
    let pendingForm = null;

    function close() {
      overlay.classList.remove("open");
      pendingForm = null;
    }

    forms.forEach((form) => {
      form.addEventListener("submit", (e) => {
        if (form.dataset.confirmed === "true") return; // allow programmatic submit through
        e.preventDefault();
        pendingForm = form;
        msgEl.textContent = form.getAttribute("data-confirm") || "This action cannot be undone.";
        overlay.classList.add("open");
      });
    });

    okBtn.addEventListener("click", () => {
      if (pendingForm) {
        pendingForm.dataset.confirmed = "true";
        pendingForm.submit();
      }
      close();
    });
    cancelBtn.addEventListener("click", close);
    overlay.addEventListener("click", (e) => { if (e.target === overlay) close(); });
    document.addEventListener("keydown", (e) => { if (e.key === "Escape") close(); });
  }

  /* ---------- Reading progress bar (blog detail page) ---------- */
  function initReadingProgress() {
    const bar = document.querySelector(".progress-bar");
    if (!bar) return;
    const onScroll = () => {
      const scrollTop = window.scrollY;
      const docHeight = document.documentElement.scrollHeight - window.innerHeight;
      const pct = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
      bar.style.width = Math.min(100, Math.max(0, pct)) + "%";
    };
    document.addEventListener("scroll", onScroll, { passive: true });
    onScroll();
  }

  /* ---------- Toast ---------- */
  function showToast(message) {
    let toast = document.querySelector(".toast");
    if (!toast) {
      toast = document.createElement("div");
      toast.className = "toast";
      document.body.appendChild(toast);
    }
    toast.textContent = message;
    toast.classList.add("show");
    clearTimeout(toast._timer);
    toast._timer = setTimeout(() => toast.classList.remove("show"), 2600);
  }
  window.showToast = showToast;

  /* ---------- Init ---------- */
  document.addEventListener("DOMContentLoaded", () => {
    initTheme();
    initPasswordToggle();
    initCharCounters();
    colorizeGenreTags();
    initConfirmModal();
    initReadingProgress();
  });
})();
