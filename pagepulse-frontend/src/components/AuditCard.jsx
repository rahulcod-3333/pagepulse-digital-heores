function AuditCard({ result }) {

    const getStatusColor = (status) => {

        if (status >= 200 && status < 300) {
            return "text-green-600";
        }

        if (status >= 300 && status < 400) {
            return "text-yellow-500";
        }

        return "text-red-600";
    };

    return (

        <div className="mt-10 bg-white rounded-2xl shadow-lg overflow-hidden">

            {/* Header */}

            <div className="bg-blue-600 text-white p-8">

                <h2 className="text-3xl font-bold">
                    {result.title || "No Title Found"}
                </h2>

                <p className="mt-3 text-blue-100">
                    {result.metaDescription || "No meta description available."}
                </p>

            </div>

            {/* Statistics */}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 p-8">

                <StatCard
                    title="HTTP Status"
                    value={result.status}
                    valueClass={getStatusColor(result.status)}
                />

                <StatCard
                    title="Response Time"
                    value={`${result.responseTime} ms`}
                />

                <StatCard
                    title="H1 Tags"
                    value={result.h1Count}
                />

                <StatCard
                    title="Missing ALT Images"
                    value={result.missingAltImages}
                />

                <StatCard
                    title="Word Count"
                    value={result.wordCount}
                />

            </div>

        </div>

    );

}

function StatCard({ title, value, valueClass = "" }) {

    return (

        <div className="rounded-xl border border-gray-200 p-6 hover:shadow-md transition">

            <p className="text-gray-500 text-sm">
                {title}
            </p>

            <h3 className={`mt-2 text-3xl font-bold ${valueClass}`}>
                {value}
            </h3>

        </div>

    );

}

export default AuditCard;