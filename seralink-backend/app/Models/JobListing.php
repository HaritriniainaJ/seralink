<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class JobListing extends Model {
    protected $fillable = [
        'client_id', 'title', 'description', 'category',
        'budget_min', 'budget_max', 'budget_type', 'status', 'deadline'
    ];

    public function client() { return $this->belongsTo(User::class, 'client_id'); }
    public function proposals() { return $this->hasMany(Proposal::class); }
    public function contract() { return $this->hasOne(Contract::class); }
}