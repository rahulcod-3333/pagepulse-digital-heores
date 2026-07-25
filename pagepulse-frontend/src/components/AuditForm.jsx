import { useState } from "react";
import api from "../services/api";

function AuditForm({ setResult, setLoading, setError }) {

    const [url, setUrl] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();

        if (!url.trim()) {
            setError("Please enter a website URL.");
            return;
        }

        try {

            setLoading(true);
            setError("");
            setResult(null);

            const response = await api.post("/audit", {
                url: url.trim(),
            });

            setResult(response.data);

        } catch (error) {

            setError(
                error.response?.data?.message ||
                "Something went wrong."
            );

        } finally {

            setLoading(false);

        }

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="bg-white mt-10 rounded-xl shadow-lg p-8"
        >

            <label className="block mb-2 font-semibold">
                Website URL
            </label>

            <input
                type="text"
                placeholder="https://example.com"
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                className="w-full border rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />

            <button
                type="submit"
                className="mt-6 w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition"
            >
                Analyze Website
            </button>

        </form>

    );

}

export default AuditForm;
