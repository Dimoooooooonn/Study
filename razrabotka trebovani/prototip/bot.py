from telegram import Update
from telegram.ext import (
    ApplicationBuilder,
    CommandHandler,
    ContextTypes
)

from config import BOT_TOKEN
from hh_parser import get_vacancies
from storage import save_filters, get_filters, is_sent, mark_sent


async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    await update.message.reply_text(
        "👋 Привет! Я SearchAllJob.\n"
        "Команды:\n"
        "/set python 100000 — задать фильтры\n"
        "/search — найти вакансии"
    )


async def set_filters(update: Update, context: ContextTypes.DEFAULT_TYPE):
    try:
        text = context.args[0]
        salary = int(context.args[1])

        save_filters(update.effective_user.id, text, salary)
        await update.message.reply_text("✅ Фильтры сохранены")

    except (IndexError, ValueError):
        await update.message.reply_text(
            "❌ Формат: /set <ключевое_слово> <зарплата>"
        )


async def search(update: Update, context: ContextTypes.DEFAULT_TYPE):
    filters = get_filters(update.effective_user.id)

    if not filters:
        await update.message.reply_text("⚠️ Сначала задай фильтры: /set")
        return

    vacancies = get_vacancies(
        filters["text"],
        filters["salary"]
    )

    if not vacancies:
        await update.message.reply_text("❌ Вакансии не найдены")
        return

    for v in vacancies:
        if is_sent(v["id"]):
            continue

        message = (
            f"💼 {v['title']}\n"
            f"🏢 {v['company']}\n"
            f"💰 от {v['salary']} ₽\n"
            f"🔗 {v['url']}"
        )

        await update.message.reply_text(message)
        mark_sent(v["id"])


def main():
    app = ApplicationBuilder().token(BOT_TOKEN).build()

    app.add_handler(CommandHandler("start", start))
    app.add_handler(CommandHandler("set", set_filters))
    app.add_handler(CommandHandler("search", search))

    print("🚀 SearchAllJob запущен")
    app.run_polling()


if __name__ == "__main__":
    main()
