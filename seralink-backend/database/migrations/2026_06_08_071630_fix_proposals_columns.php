<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::table('proposals', function (Blueprint $table) {
            $table->renameColumn('amount', 'budget');
            $table->date('deadline')->nullable()->after('budget');
            $table->dropColumn('delivery_days');
        });
    }
    public function down(): void {
        Schema::table('proposals', function (Blueprint $table) {
            $table->renameColumn('budget', 'amount');
            $table->unsignedInteger('delivery_days')->after('amount');
            $table->dropColumn('deadline');
        });
    }
};